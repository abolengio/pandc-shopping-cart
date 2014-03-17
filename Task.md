## The Task

Create a restful, json-based Web-Service for a very simple shopping cart. Basic functions are: adding
an item, removing an item, listing all items in the cart. Also if the current time is within the
rebate_timeframe of a product (see data below) make sure to give 20% discount on the price of the
product. All users share the same cart, so there is no need for session management. Store the
contents of the cart to a file in the filesystem, don't use a database. Given is a file with a list of
products which can be added to the cart (see below).
You may use any tools that you like to accomplish this task, including build/dependency
management, IDE/editor, libraries, etc.
We should be able to run your solution without needing to make any changes, with a single
command, an available internet connection, an up-to-date Java SDK and the build tool of your choice,
which we will install and setup.
You should write suitable tests that confirm that the code you have written is working as it should.
All source code to the solution should be provided as a buildable, testable project.

products.csv:

    product_id,name,price,rebate_timeframe
    1001,Dress with pink flowers,29.99,2014-02-28:15:00:00-2014-02-28:16:00:00
    1002,Green Shirt,9.90,
    1003,Blue Trousers,14.50,
    1004,Brown Bag,5.00,
