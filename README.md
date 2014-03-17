## Getting Started

To build the project you will need Java 7 and maven 3.

To execute automated tests:

    mvn clean test

To start the service:

    mvn jetty:run

The product.csv which is used by the service as a product catalogue is located in the root folder of the project.

The content of the shopping cart is stored in the file shopping.cart.csv which is also located in the root folder of the project. Feel free to remove the shopping cart file to reset the state of the shopping cart.

## Usage

To see the content of the cart please start the service and go to

[http://localhost:8080/v1/cart]

How to interact with the service further should be clear from the [HATEOAS](http://en.wikipedia.org/wiki/HATEOAS) links.

Operations of adding a new product to the shopping cart or updating quantity of specific item take JSON in the format:

    {
        "productId":"some-id",
        "quantity":not-negative-number
    }

## Assumptions

I had to make a number of assumptions about the requirements and domain while working on the task. Here are some of my assumptions, followed by details of the solution based on this assumptions (in brackets).

* (Meta assumption) I do not have [instant] access to the "product owner" so I should come up with some assumptions rather than clarifying requirements.
* It should only be possible to add products to shopping cart which exist in the product catalogue i.e. which are present in products.csv file
* The rebate price is calculated based on the time the shopping cart is retrieved rather than on the time the product is added to the cart
* All prices are in Euro, no currency conversion is required
* All users of the system are based in GMT+1 time zone. (This time zone is hard-coded in the <code>com.ab.cart.utils.Preferences</code> class as a constant.)
* The service is to be hosted on a single machine, with a single instance of the app running on the machine. (Access to the shopping cart file is synchronised only in single JVM)
* No pagination is required for the content of the cart as the cart is not ever too big
* Products are never removed from the product catalogue and availability of products is never an issue, i.e. stock is unlimited.
* The service is to be used inside a private network i.e. not exposed to public so no authentication and authorisation is required
* Sub total for the whole cart is useful
* Product catalogue and shopping cart services are separate services

## Notes about solution

* the API is explicitly versioned in the URI to support evolution of the API in future (It seems to be specially important given how many assumptions I had to make)
* catalogue service is local service while in real life it would be probably remote one


