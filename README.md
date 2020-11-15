# todos

This is the todos project.

## Running the project

```
> lein do clean, uberjar
> java -cp target/todos.jar clojure.main -m todos.server
```

The application will now be available at [http://localhost:3000](http://localhost:3000)

## Running in development mode

```
> lein run dev
```