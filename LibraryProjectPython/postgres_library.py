import psycopg2
import threading
import time

conn = psycopg2.connect(host="localhost", dbname="postgres", user="postgres", password="Goldpassword123", port=5432)

conn.autocommit = False  # Disable autocommit for manual transaction control


# PostgreSQL does NOT allow Dirty Read, so this test is not necessary!

def unrepeatable_read():
    """ Simulates Unrepeatable Read """
    cursor = conn.cursor()
    cursor.execute("SET TRANSACTION ISOLATION LEVEL READ COMMITTED;")  # A transaction can only read committed data—it
                                                                       # cannot read data from another transaction that has not yet
                                                                       # been committed.

    print("\n[Session 1] Initial read...")
    cursor.execute("SELECT author FROM book WHERE title = 'The Idiot';")
    initial_author = cursor.fetchone()
    print("[Session 1] Initial author:", initial_author)

    def update_author():
        time.sleep(1)
        cursor2 = conn.cursor()
        cursor2.execute("UPDATE book SET author = 'Lev Tolstoy' WHERE title = 'The Idiot';")
        conn.commit()
        print("[Session 2] Updated the author to Lev Tolstoy")
        cursor2.close()

    t = threading.Thread(target=update_author)
    t.start()

    time.sleep(2)

    print("[Session 1] Read after modification...")
    cursor.execute("SELECT author FROM book WHERE title = 'The Idiot';")
    new_author = cursor.fetchone()
    print("[Session 1] Author after modification:", new_author)

    cursor.close()
    conn.commit()


def phantom_read():
    """ Simulates Phantom Read with locking """
    cursor = conn.cursor()
    cursor.execute("SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;")  # Prevents dirty reads,
                                                                        # Prevents unrepeatable reads,
                                                                        # Allows phantom reads

    print("\n[Session 1] Initial read (with LOCKING)...")

    # ✅ SELECT without COUNT(*), but with FOR UPDATE
    cursor.execute("SELECT * FROM borrowed WHERE userid = 1 FOR UPDATE;")
    initial_rows = cursor.fetchall()
    print("[Session 1] Initial number of borrowed books for user 1:", len(initial_rows))

    def insert_new_purchase():
        time.sleep(2)
        cursor2 = conn.cursor()

        # ✅ Using user_id = 1, which already exists
        cursor2.execute("INSERT INTO borrowed (id, bookid, userid) VALUES (2, 2, 1);")
        conn.commit()
        print("[Session 2] Added a new row to borrowed books.")
        cursor2.close()

    t = threading.Thread(target=insert_new_purchase)
    t.start()

    time.sleep(3)

    print("[Session 1] Read after modification...")
    cursor.execute("SELECT * FROM borrowed WHERE userid = 1;")
    new_rows = cursor.fetchall()
    print("[Session 1] Final number of borrowed books:", len(new_rows))

    cursor.close()
    conn.commit()


def lost_update():
    """ Simulates Lost Update """
    cursor = conn.cursor()
    cursor.execute("SET TRANSACTION ISOLATION LEVEL READ COMMITTED;")  # A transaction can only read committed data—it
    # cannot read data from another transaction that has not yet
    # been committed.

    print("\n[Session 1] Initial read...")
    cursor.execute("SELECT title FROM book WHERE id = 1;")
    initial_title = cursor.fetchone()
    print("[Session 1] Initial title:", initial_title)

    def update_title():
        time.sleep(1)
        cursor2 = conn.cursor()
        cursor2.execute("UPDATE book SET title = 'Abracadabra' WHERE id = 1;")
        conn.commit()
        print("[Session 2] Updated the title to 'Abracadabra'")
        cursor2.close()

    t = threading.Thread(target=update_title)
    t.start()

    time.sleep(2)

    print("[Session 1] Updating to 'White Nights'")
    cursor.execute("UPDATE book SET title = 'White Nights' WHERE id = 1;")
    cursor.execute("SELECT title FROM book WHERE id = 1;")
    conn.commit()

    print("[Session 1] Final title:", cursor.fetchone())

    cursor.close()


def uncommitted_dependency():
    """ Simulates Uncommitted Dependency """
    cursor = conn.cursor()
    cursor.execute("SET TRANSACTION ISOLATION LEVEL READ COMMITTED;")  # A transaction can only read committed data—it
                                                                       # cannot read data from another transaction that has not yet
                                                                       # been committed.

    print("\n[Session 1] Insert without commit...")

    cursor.execute("INSERT INTO borrowed (id, bookid, userid) VALUES (3, 3, 1);")

    def read_uncommitted():
        time.sleep(1)
        cursor2 = conn.cursor()
        cursor2.execute("SELECT * FROM borrowed WHERE userid = 1;")
        print("[Session 2] Read borrowed books:", cursor2.fetchall())
        cursor2.close()

    t = threading.Thread(target=read_uncommitted)
    t.start()

    time.sleep(2)
    print("[Session 1] Performing ROLLBACK...")
    conn.rollback()
    cursor.execute("SELECT * FROM borrowed WHERE userid = 1;")
    print("[Session 1] Read borrowed books:", cursor.fetchall())

    cursor.close()


# Running tests in PostgreSQL
# print("\n=== Test Unrepeatable Read ===")
# unrepeatable_read()
# print("\n=== Test Phantom Read ===")
# phantom_read()
# print("\n=== Test Lost Update ===")
# lost_update()
print("\n=== Test Uncommitted Dependency ===")
uncommitted_dependency()

# cur = conn.cursor()

# cur.execute("""CREATE TABLE IF NOT EXISTS book (
#     id INT PRIMARY KEY,
#     title VARCHAR(200),
#     author VARCHAR(100),
#     summary VARCHAR(1000),
#     isbn VARCHAR(100),
#     availability bool);
# """)
#
# cur.execute("""CREATE TABLE IF NOT EXISTS libraryUser (
#     id INT PRIMARY KEY,
#     username VARCHAR(100),
#     email VARCHAR(100),
#     userPassword VARCHAR(100),
#     userRole VARCHAR(100));
# """)
#
# cur.execute("""CREATE TABLE IF NOT EXISTS borrowed (
#     id INT PRIMARY KEY,
#     bookId INT,
#     userId INT
# );
# """)
#
# cur.execute("""CREATE TABLE IF NOT EXISTS review (
#     id INT PRIMARY KEY,
#     userId INT,
#     bookId INT,
#     rating FLOAT
# );
# """)

# cur.execute("""INSERT INTO person (id, name, age) VALUES
# (2, 'Sara', 19);
# """)

# conn.commit()
# cur.close()
# conn.close()
