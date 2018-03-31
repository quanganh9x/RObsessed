package com.quanganh9x.rss;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import javax.print.Doc;
import java.io.*;
import java.util.*;


public class XMLHandler {

    public String path;
    public Document document;
    public Element root;

    public void setPath() {
    }
    public void read() {
        try {
            File inputFile = new File(path);
            SAXBuilder sax = new SAXBuilder();
            Document doc = sax.build(inputFile);
            this.document = doc;
            this.root = doc.getRootElement();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /*
<?xml version = "1.0"?>
<cars> // root elm (0)
   <supercars company = "Ferrari"> // 1
      <carname type = "formula one">Ferarri 101</carname> // 2
      <carname type = "sports car">Ferarri 201</carname>
      <carname type = "sports car">Ferarri 301</carname>
   </supercars>

   <supercars company = "Lamborgini">
      <carname>Lamborgini 001</carname>
      <carname>Lamborgini 002</carname>
      <carname>Lamborgini 003</carname>
   </supercars>

   <luxurycars company = "Benteley">
      <carname>Benteley 1</carname>
      <carname>Benteley 2</carname>
      <carname>Benteley 3</carname>
   </luxurycars>
</cars>
trace ("supercars", "carname") => size 2, not null, not ret, from.getChild("supercars") ret carname
     */
    public List<Element> trace(List<String> elements) { // always point to root element
        int length = elements.size(); // 2
        int i = 0;
        if (i >= length) return null; // not null
        else if (length == 1) return this.root.getChildren(elements.get(0)); // supercars, not ret
        else {
            Element list = this.root;
            while (i < length-1) { // 0<1, 1 time
                list = list.getChild(elements.get(i)); // supercars
                i++;
            }
            return list.getChildren(); // carname []
        }
    }
    public List<Element> tracefromElement(List<String> elements, Element from) { // always point to root element
        int length = elements.size(); // 2
        int i = 0;
        if (i >= length) return null; // not null
        else if (length == 1) return from.getChildren(elements.get(0)); // supercars, not ret
        else {
            Element list = from;
            while (i < length-1) { // 0<1, 1 time
                list = list.getChild(elements.get(i)); // supercars
                i++;
            }
            return list.getChildren(); // carname []
        }
    }
    public Attribute getAttrElement(Element element, String attrname) {
        return element.getAttribute(attrname);
    }
    public String getChildName(Element element, String name) {
        return element.getChildText(name);
    }
    public Element getValueList(List<Element> list, int num) {
        return list.get(num);
    }
}