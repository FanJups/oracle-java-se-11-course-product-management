# oracle-java-se-11-course-product-management
Oracle Java SE 11 Course Product Management

I'm preparing  Oracle Certified Professional JAVA SE 11 Developer so I'm following this course.

This is a copy  based on my experience.

The instructor used a linux OS and NetBeans IDE. My setup is based on windows, Eclipse IDE and Maven as my build tool.

We used different setups so I have to adapt.

This situation forced me to make little changes on my code related to how windows manage paths.

Windows doesn't allow ' : ' in file's name so I replace that character with ' _ ' using String method -> replace.

Secondly, the default locale of instructor's computer is english, mine is french. Thus, I set my default locale to english to meet the requirements.

You'll better understand what I mean by following the course and/or check code.

RESEARCHES.md shows some links used to solve the problems I faced. One problem is how to manage jlink, the instructor used built-in jlink configuration on NetBeans and
I used jlink maven plugin.

Hope it helps!
