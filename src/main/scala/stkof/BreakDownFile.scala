package stkof

import scala.io.Source
import java.io.File
import java.io.FileOutputStream

object BreakDownFile extends App {

   val xmlFile = args(0)
   val outputLocation = new File(args(1))

   for (line <- Source.fromFile(xmlFile).getLines) {
      // Write each line to a file
      writePost(line.mkString)
   }

   
   def writePost(str:String) = {
      
      val rowID = str.split("=")(1).split(" ")(0).split("\"")(1)
      
      // Use the rowID as the small xml file name
      val outputFile = new File(outputLocation, rowID + ".xml")
      // println("writting to: " + outputFile.getAbsolutePath())

      val out = new FileOutputStream(outputFile)
      out.write(str.getBytes())
      out.close
   }

}

