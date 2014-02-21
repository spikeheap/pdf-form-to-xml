package uk.ox.brc.pdfforms

import groovy.xml.MarkupBuilder
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm
import org.apache.pdfbox.util.PDFTextStripper

/**
 * Created by rb on 21/02/2014.
 */
class Extractor {
    public static void main(String[] args){
        Extractor ex = new Extractor()
        args.each { file ->
            ex.extract(new File(file))
        }
    }

    def documentText = null
    PDAcroForm form = null
    def formContents = [:]

    void extract(File file){
        PDDocument pdDoc = PDDocument.load(file);

        // Grab the entire form text and drop it into the variable
        PDFTextStripper reader = new PDFTextStripper();
        documentText = reader.getText(pdDoc);

        // Grab the list of form fields to populate "fields"
        form = pdDoc.documentCatalog.acroForm

        form.fields.each { field ->
            def fieldName = field.alternateFieldName
            if(fieldName){
                formContents[fieldName[0..-2]] = field.value
            }
        }
    }

    String toXML(){
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.form{
            formContents.each { key, value ->
                // Remove the last char ":" from the key
                "${key}""${value}"
            }
        }


        return writer.toString()
    }
}
