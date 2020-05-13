# karaoke-application

To compile:
javac --module-path $JAVAFX_HOME --add-modules javafx.controls,javafx.media *.java

To run:
java --module-path $JAVAFX_HOME --add-modules javafx.controls,javafx.media KaraokeApplication

To test:
java --module-path $JAVAFX_HOME --add-modules javafx.controls,javafx.media org.junit.runner.JUnitCore KaraokeApplicationTester
