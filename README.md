# Assignment 1

## Usage

It is important to note that both the backend and frontend are considered to be separate applications. One can run without the other with no problem (except for the api requests that will always throw an error if the backend is not on). 

Frontend - runs on port 3000

Backend - runs on port 8080

MySQL - runs on port 3306


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
##### 3. Make sure that all the dependencies are added to your classpath project

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

##### 4. Add the variable `DB_USERNAME` to your environment files. This should be the username you use to connect to your MySQL database.
##### 5. Add the variable `DB_PASSWORD` to your environment files. This should be the password you use to connect to your MySQL database.
##### 6. Add the variable `DB_URL` to your environment files. This should be the url you use to connect to your MySQL database.
##### 7. Compile the code and start the server

-> Right click on project

-> Hover on `Run As`

-> Select `Run on Server`

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

