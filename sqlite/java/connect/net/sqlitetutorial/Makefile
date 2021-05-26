JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
					$(JC) $(JFLAGS) $*.java

CLASSES = \
					Connect.java \
					Login.java \
        	CreateAccount.java \
        	Main.java \
					Manager.java \
        	Customer.java \
					ConfirmLogin.java \
					Exceptions.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
					$(RM) *.class