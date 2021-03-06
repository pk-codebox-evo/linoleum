
Required software:

- jdk 1.7 ( http://www.oracle.com/technetwork/java/index.html )

Optional software:

- jmf 2.1.1e ( http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-java-client-419417.html )
- java3d 1.5.1 ( same as above )
- jai 1.1.2_01 ( same as above )


To run linoleum, add dist/bin to your path, give dist/bin/linoleum execution privilege (unix), then:
  linoleum


Once in linoleum, to execute a shell:
  File->Open
  double-click on "ScriptShell"


To install an application (or any Ivy module), in the script shell:
  install("org#module;version");


Alternatively:
  open "Packages"
  enter the (org, module, version) triplet
  click "Install"


To build linoleum from itself, first clone the repository by external means (for now), then in the script shell:
  cd("/path/to/linoleum");
  load("build.js")


To publish an application:

- add net.java.linoleum#application;1.2 to your project's dependencies
- extend linoleum.application.Frame
- make it available to the service loader in META-INF/services/javax.swing.JInternalFrame
- publish your artifact in maven central
- let me know so that I put it in the list (below)


To use linoleum as your desktop environment in Linux:
  wget https://java.net/downloads/linoleum/linoleum.deb
  sudo dpkg -i linoleum.deb


List of applications

  net.java.linoleum#j3d;1.2		3D Object Loader (requires java3d)
  net.java.linoleum#jcterm;0.0.11	SSH2 Terminal Emulator in Pure Java
  net.java.linoleum#media;1.2		Media Player (requires jmf)
  net.java.linoleum#pdfview;1.2		PDF viewer (may require jai in some cases)


Useful libraries

  org.bouncycastle#bcpg-jdk15;1.45	The Bouncy Castle Java API for handling the OpenPGP protocol
  mstor#mstor;0.9.9			A JavaMail provider for persistent email storage

