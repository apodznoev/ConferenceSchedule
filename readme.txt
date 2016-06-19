This application was written according to Problem Two: Conference Track Management by ThoughtWorks.

Given version allows to generate conference schedule based on provided list of lectures.
Currently application works only as interactive console only:
user is prompted for some input and after entering application will react according to current state.

All instructions and helps are written during interactions.
Program intended to use with standard input-output, it's not guarantied to work with streams redirection.

Ant building tool is recommended for assembling and running the application.
The easiest way to launch the application:
    1. type 'ant' from root project directory
            in that case program will be performed during ant building cycle, thus using it's JVM.
            It can case some tricky problems, but they are not expected.
Another way is to:
    1. type 'ant jar' from root project directory
    2. type 'java -classpath build/lib/ctm.jar com.thoughtworks.apodznoev.ctm.console.ConsoleLauncher' from root project directory
            in that case program will be executed normally in separate JVM.
