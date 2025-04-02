DROP database grails_db;
CREATE database grails_db;
use grails_db;

SELECT * FROM book;
SELECT * FROM author;
SELECT * FROM category;
SELECT * FROM book_category;

DELETE FROM author WHERE id = 31;
DELETE FROM category WHERE id > 3;