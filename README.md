# Saxy

Extension classes for Saxon 8.7.

## Usage

API documentation can be read [online](http://cchantep.github.io/saxy/).

### XHTMLEmitter

Based on `net.sf.saxon.serialize.XHTMLEmitter`, it changes emission of following tags so that, in case of empty content, only following tags are rendered as self-closing: `link`, `meta`, `hr`, `br`, `wbr`, `img`, `param`, `embed`, `video`, `audio`, `source`, `track`, `area`, `rect`, `input`, `button`.

To use it while transforming XML document, you should add following `xsl:output` in the XSL stylesheet:

```xml
  <xsl:output xmlns:s="uri:saxy"
              method="s:saxy.XHTMLEmitter"
              omit-xml-declaration="yes" />
```

It also removes comments, except for those starting with `[` and ending with `]`, so that browser conditionals (e.g. `<!--[if lt IE 9]>…<![endif]-->`) are preserved.

If `doctype-public` attribute is used with `html` value, `<!DOCTYPE html>` is prepended to output.

```xml
<xsl:output xmlns:s="uri:saxy"
            method="s:saxy.XHTMLEmitter"
            doctype-public="html"
            omit-xml-declaration="yes" />
```

### StripXHTMLEmitter

This emitter is similar to base `XHTMLEmitter`, but it strip extraneous whitespaces:

- multiple whitespaces reduced to a single one,
- element strip to empty/self-closing one when content is whitespace only.

To use it for transformation, following `xsl:output` should be added to the stylesheet:

```xml
<xsl:output xmlns:s="uri:saxy"
            method="s:saxy.StripXHTMLEmitter" 
            doctype-public="html"
            omit-xml-declaration="yes" />
```

If you just want to strip XHTML document using such emitter, without more transformation, you can use stylesheet similar to following one:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
  <xsl:output xmlns:s="uri:saxy"
              method="s:saxy.StripXHTMLEmitter" 
              doctype-public="html"
              omit-xml-declaration="yes" />

  <xsl:template match="/"><xsl:copy-of select="." /></xsl:template>
</xsl:stylesheet>
```

### Maven

Using Maven 2/3+, Saxy dependency can be resolved as following from your POM:

```xml
<!-- ... -->

  <repositories>
    <!-- ... -->
    <repository>
      <id>applicius-snapshots</id>
      <name>Applicius Maven2 Snapshots Repository</name>
      <url>https://raw.github.com/applicius/mvn-repo/master/snapshots/</url>
    </repository>
  </repositories>

  <dependencies>
    <!-- ... -->
    <dependency>
      <groupId>cchantep</groupId>
      <artifactId>saxy</artifactId>
      <version>VERSION</version><!-- 1.0.0 -->
    </dependency>
  </dependencies>

```

If you are using `maven-xml-plugin` to transform XML files during build, you can add dependency mentionned just before inside plugin definition:

```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>xml-maven-plugin</artifactId>
  <version>1.0</version>
  <executions>
    <execution>
      <phase>process-sources</phase>
      <goals>
        <goal>transform</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <transformationSets>
      <transformationSet>
        <dir>src/main/webapp</dir>
        <includes>
          <include>*.xhtml</include>
        </includes>
        <!-- XSL stylesheet declaring Saxy output -->
        <stylesheet>${basedir}/src/main/xsl/saxy.xsl</stylesheet>
        <outputDir>${project.build.directory}/web</outputDir>
          <fileMappers>
            <fileMapper implementation="org.codehaus.plexus.components.io.filemappers.FileExtensionMapper">
            <targetExtension>.html</targetExtension>
          </fileMapper>
        </fileMappers>
      </transformationSet>
    </transformationSets>
  </configuration>
  <dependencies>
    <dependency>
      <groupId>net.sf.saxon</groupId>
      <artifactId>saxon</artifactId>
      <version>8.7</version>
    </dependency>
    <dependency>
      <groupId>cchantep</groupId>
      <artifactId>saxy</artifactId>
      <version>1.0.0</version>
    </dependency>
  </dependencies>
</plugin>
```

## Build

`sbt publish`