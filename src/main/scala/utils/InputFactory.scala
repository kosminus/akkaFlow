package utils

import config.Configuration

import java.io.File

object InputFactory extends App {

  def recursiveListFiles(base: File, extension: String): Seq[File] = {
    val files = base.listFiles
    val filesByExtension = files.filter(_.getName.endsWith(extension))
    filesByExtension ++
      files
        .filter(_.isDirectory)
        .flatMap(recursiveListFiles(_, extension))
  }

  def isDirectory(f: File): Boolean = f.isDirectory

  def getInputList(): Seq[File] = {
    val conf = Seq(Configuration.inputConfig.getString("filepath"))
    val extension = Configuration.inputConfig.getString("type")
    conf
      .map(s => new File(s))
      .flatMap(f => if (f.isDirectory) recursiveListFiles(f, extension)
      else Seq(f))
  }


}