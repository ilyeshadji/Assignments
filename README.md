# Assignment 1

## Installation

### Frontend

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

##### 4. Compile the code and start the server

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

