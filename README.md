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

OR See section below to run the web application with the backend (served by JSP)

### Database (MySQL)

If you don't have this already installed on your personal device, I highly suggest referring to this documentation to set it up!

https://dev.mysql.com/doc/mysql-getting-started/en/#mysql-getting-started-installing

###### 1. Create a database named `Assignments`

### Backend (JAVA)

##### 1. Ask Ilyes Hadji for the source files.
##### 2. Open the source files in the IDE of your choice (preferably ECLIPSE)
##### 3. Make sure that all the dependencies are added to the `Modulepath` of the project

* jackson-annotations-2.15.2
* jackson-core-2.15.2
* jackson-databind-2.15.2
* jsp-api-2.2
* jstl
* mysql-connector-java-8.0.13
* jsp-api
* servlet-api
* tomcat-jdbc
* jjwt-api
* jjwt-impl
* jjwt-jackson
* jBCrypt

To do so, you can follow these steps for each library:

**NB:** Some of these library can be selected internally, meaning that you could simply add them from the `Add JAR` button on step 8

1. Download the library (here is a useful link: http://www.java2s.com/ref/jar/jar.html)
2. Copy and paste the .jar file in this folder `./src/main/webapp/WEB-INF/lib`
3. Right click on the project opened in the `Project Explorer`
4. Hover over `Build Path`
5. Click on `Configure Build Path`
6. Navigate to the `Libraries` tab
7. Click on `Modulepath`, dropdown menu
8. Click on `Add External JAR`
9. Navigate to the `lib` folder in which you copied the library (jar file)
10. Select the library
11. Click on `Open`
12. Click on `Apply and Close`

##### 4. Add the variable `DB_USERNAME` to your environment files. This should be the username you use to connect to your MySQL database.
##### 5. Add the variable `DB_PASSWORD` to your environment files. This should be the password you use to connect to your MySQL database.
##### 6. Add the variable `DB_URL` to your environment files. This should be the url you use to connect to your MySQL database.

Here is an example on how you could add a variable to your environment using `Eclipse`

1. Click on the `RUN` dropdown on your top menu
2. Click on the `Run Configurations` option
3. Select the `Environment` tab
4. On the right, you will see an `Add` button, click on it
5. Write the name of the variable (`DB_USERNAME` for example)
6. Write the value of the variable (`root` for example)
7. Click on `OK`
8. Click on `Apply` on the bottom right.

##### 7. Compile the code and start the server

-> Right click on project

-> Hover on `Run As`

-> Select `Run on Server`

##### 7. Add the variable `SRC_PATH` to your environment files. This should be the path to the `src` folder of this project.

## **NB:** There is a key.pem in the source files; RSA key for the generation of the token, this should never be there for security concerns

### Backend (JAVA) + Frontend

##### 1. Perform all the steps before `npm start` from the frontend section
##### 2. Build the project

```bash
npm run build
```

##### 4. Navigate to `src/frontend/build/static/js` and copy the name of the file that starts with `main`

e.g. `main.5e6f9ad3.js`

##### 4. Navigate to `src/main/webapp/index.jsp` and paste the string in the previous step in the `script` tag to change the `src` to the actual path of your build file

##### 5. Copy the `build` folder in the `frontend` directory

##### 6. Paste the folder in the `webapp` directory

##### 7. Compile and run as server like described in the `Backend (JAVA)` section of the README

