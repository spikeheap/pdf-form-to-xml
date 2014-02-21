package uk.ox.brc.pdfforms

import org.apache.pdfbox.pdmodel.interactive.form.PDField
import spock.lang.Specification

/**
 * Created by rb on 21/02/2014.
 */
class ExtractorTest extends Specification {

    def "Loads a PDF file and extracts the birth date"(){

        when: "We load a PDF file"
        Extractor ex = new Extractor()
        ex.extract(new File(filename))

        then: "The extracted text contains Birth Date"
        ex.getDocumentText().contains("Birth Date:")

        where:
        filename << ["src/test/resources/lymphoma-mdt-1-5-test-data.pdf"]
    }

    def "Loading a PDF gives back the form structure including some known fields"(){
        when: "We load a PDF file"
        Extractor ex = new Extractor()
        ex.extract(new File(filename))

        def label = "Birth Date:"
        def formValue = "04/05/1976"
        def fieldId = "CR0100"

        PDField field = ex.form.getField(fieldId)

        then: "The extracted form contains the label"
        field.alternateFieldName == label

        and: "The extracted map contains the label"
        ex.formContents["Birth Date"]

        and: "The value for that label is correctly filled"
        field.value == formValue
        ex.formContents["Birth Date"] == formValue

        where:
        filename << ["src/test/resources/lymphoma-mdt-1-5-test-data.pdf"]
    }

    def "The extracted contents can be written to XML"(){

        when: "We load a PDF file"
        Extractor ex = new Extractor()
        ex.extract(new File(filename))


        println(ex.toXML())

        then:
        true
        //XMLUnit.setIgnoreWhitespace(true)
        //def xmlDiff = new Diff(writer.toString(), XmlExamples.CAR_RECORDS)
        //assert xmlDiff.similar()

        where:
        filename << ["src/test/resources/lymphoma-mdt-1-5-test-data.pdf"]
    }
}
