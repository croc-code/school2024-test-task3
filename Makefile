
# simple build exec file named main

build: all


all: main.cpp
	g++ main.cpp -o main --std=c++11 -Wall -Wextra


clear:
	rm -f *.o