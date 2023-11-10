JAVA = java
JAVAC = javac
CPTEST = -cp lib/*:bin/

MAIN_SRC = src/main/comms/Comms.java \
          src/main/member/Member.java \
          src/main/member/AcceptedProposalPair.java \
          src/main/server/MessageHandler.java \
          src/main/server/Server.java \
          src/main/App.java

MAIN_TEST = org.junit.platform.console.ConsoleLauncher
TEST_SRC = src/test/PaxosTest.java \
					src/test/MemberTest.java \
					src/test/ServerTest.java

#Compile all 
all: compile

#Compile individual components of the system
compile:
	mkdir -p bin
	javac $(CPTEST) $(MAIN_SRC) -d bin

#make test compiles entire testing harness
compile-test: compile
	@$(JAVAC) $(CPTEST) $(MAIN_SRC) $(TEST_SRC) -d bin

test: compile-test
	@$(JAVA) $(CPTEST) $(MAIN_TEST) --scan-classpath

run: compile
	java -cp bin main.App

clean:
	rm -rf bin/*
	rm -rf src/main/*/*.class
	rm -rf src/main/*.class
	rm -rf src/test/*.class
