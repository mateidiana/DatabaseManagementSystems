import mysql.connector
import threading
import time


# Function to create a new database connection
def create_connection():
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        passwd="Bill4761",
        database="library"
    )
    conn.autocommit = False  # Disable autocommit for manual transaction control
    return conn


def dirty_read():
    """Simulates Dirty Read"""

    # Session 1 (Writer)
    conn1 = create_connection()
    cursor1 = conn1.cursor()

    cursor1.execute("SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;")
    cursor1.execute("START TRANSACTION;")

    print("\n[Session 1] Updating book title for book ID 1 without committing...")
    cursor1.execute("UPDATE book SET title = 'White Nights' WHERE id = 1;")

    def read_dirty():
        """Session 2 (Reader) attempting a dirty read"""
        time.sleep(1)  # Ensure update happens first

        conn2 = create_connection()
        cursor2 = conn2.cursor()

        cursor2.execute("SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;")
        cursor2.execute("START TRANSACTION;")

        cursor2.execute("SELECT title FROM book WHERE id = 1;")
        print("[Session 2] Dirty Read Title:", cursor2.fetchone())

        cursor2.close()
        conn2.close()

    # Start a separate thread for Session 2
    t = threading.Thread(target=read_dirty)
    t.start()

    time.sleep(3)  # Ensure dirty read happens before rollback
    print("[Session 1] Performing ROLLBACK...")
    conn1.rollback()

    cursor1.execute("SELECT title FROM book WHERE id = 1;")
    print("[Session 1] Actual Title After Rollback:", cursor1.fetchone())

    cursor1.close()
    conn1.close()


def unrepeatable_read():
    """ Simulates Unrepeatable Read """
    conn1 = create_connection()
    cursor = conn1.cursor()
    cursor.execute("SET TRANSACTION ISOLATION LEVEL READ COMMITTED;")

    print("\n[Session 1] Initial read...")
    cursor.execute("SELECT author FROM book WHERE title = 'The Idiot';")
    initial_price = cursor.fetchone()
    print("[Session 1] Initial title:", initial_price)

    def update_price():
        time.sleep(1)
        conn2 = create_connection()
        cursor2 = conn2.cursor()
        cursor2.execute("UPDATE book SET author = 'Lev Tolstoy' WHERE title = 'The Idiot';")
        conn2.commit()
        print("[Session 2] Updated the author to Lev Tolstoy")
        cursor2.close()

    t = threading.Thread(target=update_price)
    t.start()

    time.sleep(2)

    print("[Session 1] Read after modification...")
    cursor.execute("SELECT author FROM book WHERE title = 'The Idiot';")
    new_date = cursor.fetchone()
    print("[Session 1] Author after modification:", new_date)

    cursor.close()
    conn1.commit()


def phantom_read():
    """Simulates Phantom Read"""
    conn1 = create_connection()
    cursor1 = conn1.cursor()

    # Change isolation level to READ COMMITTED to allow phantom reads
    cursor1.execute("SET TRANSACTION ISOLATION LEVEL READ COMMITTED;")
    cursor1.execute("START TRANSACTION;")

    print("\n[Session 1] Initial read (before insert)...")
    cursor1.execute("SELECT * FROM borrowed WHERE userid = 1;")
    initial_count = cursor1.fetchall()
    print("[Session 1] Initial number of borrowed books:", len(initial_count))

    def insert_new_borrowed():
        """Session 2: Inserts a new row while Session 1 is still open"""
        time.sleep(1)  # Ensure Session 1 reads first

        conn2 = create_connection()
        cursor2 = conn2.cursor()
        cursor2.execute("SET TRANSACTION ISOLATION LEVEL READ COMMITTED;")
        cursor2.execute("START TRANSACTION;")

        print("[Session 2] Inserting a new borrowed book record...")
        cursor2.execute("INSERT INTO borrowed (id, bookid, userid) VALUES (2, 2, 1);")
        conn2.commit()
        print("[Session 2] Insert committed.")

        cursor2.close()
        conn2.close()

    # Start a separate thread for Session 2
    t = threading.Thread(target=insert_new_borrowed)
    t.start()

    time.sleep(3)  # Ensure insert is completed before next read

    print("[Session 1] Read after insert attempt...")
    cursor1.execute("SELECT * FROM borrowed WHERE userid = 1;")
    new_count = cursor1.fetchall()
    print("[Session 1] Final number of borrowed books:", len(new_count))

    cursor1.close()
    conn1.commit()
    conn1.close()


def lost_update():
    """ Simulates Lost Update """
    conn1 = create_connection()
    cursor = conn1.cursor()
    cursor.execute("SET TRANSACTION ISOLATION LEVEL READ COMMITTED;")

    print("\n[Session 1] Initial read...")
    cursor.execute("SELECT title FROM book WHERE id = 1;")
    initial_price = cursor.fetchone()
    print("[Session 1] Initial title:", initial_price)

    def update_price():
        time.sleep(1)
        conn2 = create_connection()
        cursor2 = conn2.cursor()
        cursor2.execute("UPDATE book SET title = 'Abracadabra' WHERE id = 1;")
        conn2.commit()
        print("[Session 2] Updated the title to 'Abracadabra'")
        cursor2.close()

    t = threading.Thread(target=update_price)
    t.start()

    time.sleep(2)

    print("[Session 1] Updating to 'White Nights'")
    cursor.execute("UPDATE book SET title = 'White Nights' WHERE id = 1;")
    cursor.execute("SELECT title FROM book WHERE id = 1;")

    print("[Session 1] Final title:", cursor.fetchone())

    cursor.close()
    conn1.commit()


def uncommitted_dependency():
    """Simulates Uncommitted Dependency (Dirty Read)"""

    conn1 = create_connection()
    cursor1 = conn1.cursor()

    # Set isolation level to READ UNCOMMITTED for dirty reads
    cursor1.execute("SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;")
    cursor1.execute("START TRANSACTION;")

    print("\n[Session 1] Inserting book record without commit...")
    cursor1.execute("INSERT INTO borrowed (id, bookid, userid) VALUES (3, 3, 1);")

    def read_uncommitted():
        """Session 2: Reads uncommitted data"""
        time.sleep(1)  # Ensure Session 1 inserts first

        conn2 = create_connection()
        cursor2 = conn2.cursor()

        cursor2.execute("SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;")
        cursor2.execute("START TRANSACTION;")

        print("[Session 2] Attempting dirty read...")
        cursor2.execute("SELECT * FROM borrowed WHERE userid = 1;")
        print("[Session 2] Read borrowed books:", cursor2.fetchall())

        cursor2.close()
        conn2.close()

    # Start a separate thread for Session 2
    t = threading.Thread(target=read_uncommitted)
    t.start()

    time.sleep(3)  # Ensure dirty read happens before rollback

    print("[Session 1] Performing ROLLBACK...")
    conn1.rollback()

    cursor1.execute("SELECT * FROM borrowed WHERE userid = 1;")
    print("[Session 1] Read borrowed books after rollback:", cursor1.fetchall())

    cursor1.close()
    conn1.close()


# print("\n=== Test Dirty Read ===")
# dirty_read()
# print("\n=== Test Unrepeatable Read ===")
# unrepeatable_read()
# print("\n=== Test Phantom Read ===")
# phantom_read()
# print("\n=== Test Lost Update ===")
# lost_update()
print("\n=== Test Uncommitted Dependency ===")
uncommitted_dependency()

