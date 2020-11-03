# How to use Either Monad for input validation?

I tried with the following approach: I implemented the class [Result.kt](src/main/kotlin/org/example/Result.kt) that can be `Result.Ok<T>` or `Result.Error`.

Then i have the class [NewUser.kt](src/main/kotlin/org/example/model/NewUser.kt) that is built together with some info details. Some of them are mandatory and need validation (e.g. password). Some need only validation but are optionals (e.g, email and phone number)

In order to better express the domain I wrapped some of this data with domain specific classes:
 - [Password.kt](src/main/kotlin/org/example/model/Password.kt)
 - [Email.kt](src/main/kotlin/org/example/model/Email.kt)
 - [PhoneNumber.kt](src/main/kotlin/org/example/model/PhoneNumber.kt)
 
 Then I put their constructors as private, and I only allow to instantiate them using factory methods, like `Password.from("xxx")`. This performs input validation and returns`Result<Password>`. `Result.Ok` when valid, `Result.Error` when not valid.
 This is done to ensure that it is not possible to build a `NewUser` with, for example, an invalid password (same for email or phone number).
 
 Then to simplify the building of a `NewUser`, it has a factory method, as well, that takes raw data and returns a `Result<User>`
 
 ```kotlin
val userResult: Result<NewUser> = NewUser.with(
        username = "luca.piccinelli",
        firstname = "Luca",
        lastname = "Piccinelli",
        password = "banane",
        jobDescription = "Developer",
        email = "luca.picci@gmail.com",
        phoneNumber = "+39 34755555555"
    )
```

My problem is in the implementation of this `NewUser.with` method. I dont' exactly know how to chain the validation of `Password`, `Email` and `PhoneNumber`. What i would like to achive should be something like:

```kotlin
val user: Result<NewUser> = Password.from(passwordValue)
    .flatMap{ Email.from(emailValue) }
    .flatMap{ PhoneNumber.from(emailValue) }
    .map{ (password: Password, email: Email?, phoneNumber: PhoneNumber?) -> 
        NewUser(
            username = username,
            name = NameOfAPerson(firstname, lastname),
            password = password,
            jobDescription = jobDescription,
            contacts = UserContacts(email, phoneNumber)        
    }
```

What I ended up instead is very far from here 😅, and definitely messy (please have a look at [NewUser.kt](src/main/kotlin/org/example/model/NewUser.kt)).

Any help would be much appreciated. Thanks 😄