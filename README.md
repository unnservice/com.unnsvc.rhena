Rhena Build System Automation, the flexible build system

# License
GPL General Public License Version 3

# Purpose
It has been observed that existing build systems are inadequate for building large systems. I have made many attempts in building around the existing frameworks such as maven and grade, even going as far as building an XML framework on top of grade to automate most aspects of the build, but the level of automation was not found to be sufficient in aiding in the construction of large enterprise applications.

Rhena attempts to solve most problems you will face when dealing with 100 to 1000 projects and beyond, and do so in the most performant way by using an agent for the execution of lifecycles instead of starting new agents during the build of each module, as the other systems do. Beyond that, it allows you to have only certain modules in the workspace which may be indirectly linked to each other to enable you to only have relevant aspects of what I call a "mega model" in the workspace, the workspace which functions as a repository.

It does many other neat things and enables deep integration with the IDE, and to group modules by components in a different way than what other frameworks allow, but at present this may be far from finished. Here's what it looks like so far http://i.imgur.com/9F4JpEQ.png

# Architecture description
2 Runtimes which work in unison to produce artifacts through execution of lifecycles. You will also be able to configure Rhena to only use 1 runtime for both tasks, but this is not implemented yet.
```
+------------------------+
|    Rhena Framework     |
| +--------------------+ |
| | Model runtime      | |
| | - model resolution | |
| +--------------------+ |
| +--------------------+ |
| | Agent runtime      | |
| | - model execution  | |
| +--------------------+ |
+------------------------+
```

A lifecycle is the main building block for producing artifacts, it does so by first building a context which will mainly contain context objects available thrughout the lifecycle, and , then by executing processors which process the module by using the context as an input. Then, a generator is executed to produce the final execution output.
A fourth type of processor can be added, a command, which executes as a final command to do anything you want it to.
The lifecycle is executed in a classloader heirarchy which looks like this:
```
+------------------------------------------------+
| +-----------------------+                      |
| | Agent classloader     |                      |
| +----------+------------+                      |
|            |                                   |
| +----------+----------+                        |
| | Context classloader +---------------------+  |
| +---------------------+                     |  |
| +-----------------------------------------+ |  |
| | Processor/Generator/Command classloader +-+  |
| +-----------------------------------------+    |
+------------------------------------------------+
```

# The lifecycle
The lifecycles are written in java so you can have lifecycles in the same project!

# The module
Modules are described in a module.xml file and will look like this (Don't worry aboout all the XML, it will have schemas):
```
<?xml version="1.0" encoding="utf-8"?>
<module xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="urn:rhena:module" xmlns:prop="urn:rhena:properties"
	xmlns:dependency="urn:rhena:dependency"
	xsi:schemaLocation="urn:rhena:module http://schema.unnsvc.com/rhena/module.xsd"
        extends="com.unnsvc:parent:0.0.1">

	<meta component="com.test" version="0.0.1">

		<prop:someprop>somevalue</prop:someprop>
		<prop:anotherprop>anothervalue</prop:anotherprop>

		<lifecycle name="library">

			<context module="com.test:lifecycle:0.0.1" class="com.test.lifecycle.CustomContext">
				<resources>
					<main path="src/main/java" />
					<main path="src/main/resources" filter="true" />
					<test path="src/test/java" />
					<test path="src/test/resources" filter="true" />
				</resources>
			</context> 
    
			<processor module="com.test:lifecycle:0.0.1" class="com.test.lifecycle.CustomJavaProcessor" />
			<generator module="com.test:lifecycle:0.0.1" class="com.test.lifecycle.CustomGenerator" />
			<command module="com.test:lifecycle:0.0.1" class="com.test.lifecycle.CustomCommand" name="testCommand" />

		</lifecycle>
	</meta>

	<dependency:main module="com.test:lifecycle:0.0.1" />
	<dependency:test module="com.test:lifecycle:0.0.1" />
	<dependency:test module="org.junit:junit:4.12" />

</module>
```



# Missing features
As this framework is under development, there are many features which are incomplete, some of them include:
- Testing. This will be built as a pluggable library on top of the core framework, but support for executing commands is complete, and testing could be performed either there, or in lifecycle processors.
- Integration with maven
- Build projects without packaging them first
- Remote execution of the agent on a different machine
- ...
