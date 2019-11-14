# HW9

0. Use the same project template archive as for the previous assignment. Don't forget to update project name in pom.xml
1. Write a model of bank department working with ATMs.
2. The department should be able to create a new ATM on base of prototype ATM. Obviously, you should use Prototype pattern.
3. Subscribe the department to events “no bills/banknotes left” for each container of banknote type in each ATM. You should use pattern Observer for this part.
4. The tests should run in Travis CI and be green (pass successfully). Submit the link.

## HW8

0. Use the same project template archive as for the previous assignment. Don't forget to update project name in pom.xml
1. Write a model of ATM using a hierarchy of classes.
2. It should have containers for any arbitrary number of different cash denominations (For example: 5, 10, 20, 50 euros).
3. The ATM should support cash withdrawal requests. Use chain of responsibility pattern to fulfil the request from containers with different denominations.
4. The ATM should support request for total remaining balance. Use iterator pattern to fulfil the balance request.
5. The code and tests should run in Travis CI and be green (pass successfully). Submit the link.
