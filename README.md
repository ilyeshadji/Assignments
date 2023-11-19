# Assignment 2

## Usage

It is important to note that both the backend and frontend are considered to be separate applications. One can run without the other with no problem (except for the api requests that will always throw an error if the backend is not on).

Frontend - runs on port 3000

Backend - runs on port 8080

MySQL - runs on port 3306

You can access the staff account with these credentials:

`email`: staff@myapp.com

`password`: secret

## Installation

### Frontend (REACT)

In your terminal, go inside the folder Frontend located in the src directory like in the example below

```bash
cd src
cd frontend

```

Then install the dependencies like follows

```bash
npm install
```

Then, to start the server, you can either run

```bash
npm start
```

### Database (MySQL)

If you don't have this already installed on your personal device, I highly suggest referring to this documentation to set it up!

https://dev.mysql.com/doc/mysql-getting-started/en/#mysql-getting-started-installing

###### 1. Create a database named `Assignments`

### Backend (JAVA)

##### 1. Configurations

-   Add the variable `DB_USERNAME` to your environment files. This should be the username you use to connect to your MySQL database.

-   Add the variable `DB_PASSWORD` to your environment files. This should be the password you use to connect to your MySQL database.

-   Add the variable `DB_URL` to your environment files. This should be the url you use to connect to your MySQL database. e.g. `jdbc:mysql://localhost:3306/assignments`

-   Add the variable `SRC_PATH` to your environment files. This should be the path to the `src` folder of this project.
    e.g. `C:\Users\Ilyes\Documents\Repos\comp-287-assignements\src`

Here is an example on how you could add a variable to your environment using `Eclipse`

1. Click on the `RUN` dropdown on your top menu
2. Click on the `Run Configurations` option
3. Select the `Environment` tab
4. On the right, you will see an `Add` button, click on it
5. Write the name of the variable (`DB_USERNAME` for example)
6. Write the value of the variable (`root` for example)
7. Click on `OK`
8. Click on `Apply` on the bottom right.

##### 2. Open Eclipse IDE for Enterprise Java and Web Developers - 2023-09

##### 3. Import the maven project following these steps

-   Click on `File`
-   Click on `Import...`
-   Open the `Maven` menu
-   Click on `Existing Maven Projects`
-   Click on `Next`
-   Select the `Browse...` option
-   Navigate to the place where you cloned the repository
-   Click on `Finish`

##### 4. Install and build the project

Cleaning the project

-   Right click on the project in the `Project Explorer`
-   Hover over `Run As...`
-   Click on `Maven clean`

Installing the project

-   Right click on the project in the `Project Explorer`
-   Hover over `Run As...`
-   Click on `Maven install`

Building the project

-   Right click on the project in the `Project Explorer`
-   Hover over `Run As...`
-   Click on `Maven build`
-   In the `Goals` field, write `clean install`
-   Click on `Run`

##### 5. Run the server

-   Right click on the project in the `Project Explorer`
- 	Hover over `Run As...`
-	Click on `Run on Server`
-   Open the `Apache` menu
-   Select `Tomcat v10.0 Server`
-   Click on `Next`
-   If you do not have this version of apache on your device, click on `Download and Install...`
-   Click on `Finish`

##### You can find the Postman exported files at the root of the project to test the REST API !

## How to import collections on Postman

##### 1. Open Postman

##### 2. On the top left corner, click on the `Import` button

##### 3. Choose import from a file

##### 4. Select file `SOEN 387.postman_collection.json`

##### 5. Repeat from the files `Customer.postman_environment.json` and `Staff.postman_environment.json`

Congrats! You are now all setup to jump in and test this wonderful web application. Make sure to select one of the environments (Customer or Staff), or else you will get nasty 406 errors :(

You can switch between a regular customer, or a staff member to be able to perform the actions you would like. Be careful, some of them are not accessible depending on the environment on which you are connected.

### **NB:** You'll find key.pem in the source files; RSA key for the generation of the token, this should never be there for security concerns
