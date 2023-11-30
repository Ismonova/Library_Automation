select * from books;
SELECT B.name,BC.name
FROM books B
         INNER JOIN book_categories BC
                    ON B.book_category_id=BC.id;

-- US01 -1
select  count(id) from users; -- 4891

select count(distinct id) from users; -- 4891

select *
from books;

select * from book_categories
group by name;

SELECT * from users;

SELECT COUNT(*) from book_borrow where is_returned=0;

select count(*) book_categories
from book_categories
group by name;

SELECT COUNT(*) AS borrowed_books_number
FROM books;

select COUNT(*) book_Catagories
from books;

select name
from book_categories;

select count(*) borrowed_books_number
from books;


SELECT COUNT(*) from book_borrow where is_returned=0;

select * from book_categories;

select * from book_categories;


select isbn,year,author, description
from books
         INNER join book_categories bc on books.book_category_id = bc.id
where name in ('Clean Code');



SELECT books.isbn, books.year, books.author, books.description, bc.name as book_category
FROM books
         INNER JOIN book_categories bc ON books.book_category_id = bc.id
WHERE books.name = 'Clean Code'
;

select book_id from book_borrow
where borrowed_date is

select * from book_borrow;

SELECT  book_id, COUNT(*) AS borrow_count
FROM book_borrow
GROUP BY book_id
ORDER BY borrow_count DESC;



SELECT bb.book_id, COUNT(*) AS borrow_count, b.name AS book_name
FROM book_borrow bb
         JOIN books b ON bb.book_id = b.book_category_id
GROUP BY bb.book_id, b.name
ORDER BY borrow_count DESC
LIMIT 1;

SELECT * FROM books WHERE NAME = 'Clean Code';

select name from book_categories
where id = (select book_category_id from books
            where id = (select book_id from book_borrow group by book_id order by count(*) desc limit 1));


SELECT COUNT(*) from book_borrow where is_returned=0;

select name from book_categories;

SELECT * FROM books WHERE NAME = 'Clean Code'