compile:
	mkdir -p out
	javac -cp src -d out src/*/*.java src/*/*/*.java

run: compile
	java -cp out samplesoln.Solution

jar: compile
	cd out; jar cf ../rushhour.jar rushhour search

JAVADOC=javadoc -public -quiet \
	-windowtitle 'Rushhour API' -source 8 \
	-doctitle 'Rushhour project --- API documentation' \
	-group "Search algorithms" "search*" \
	-group "Rushhour" "rushhour*" \
	-cp ${JUNITLIB}:out \
	-link https://docs.oracle.com/javase/8/docs/api/ \
	-overview src/overview.html 


javadoc: Makefile src/*/*.java src/*/*/*.java
	${JAVADOC} -d javadoc -sourcepath src \
		search rushhour rushhour.model

spec:
	pdflatex spec

pack: jar javadoc spec
	rm -rf out
	rm -f spec.aux spec.out spec.log
	rm -f *~ */*~ */*/*~

clean:
	rm -rf out javadoc rushhour.jar spec.aux spec.out spec.log \
		*~ */*~ */*/*~

