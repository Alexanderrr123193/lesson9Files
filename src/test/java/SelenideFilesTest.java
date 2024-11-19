import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import model.Client;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;
import utils.FileUnpacker;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenideFilesTest {

    FileUnpacker fileUnpacker = new FileUnpacker();

    @Test
    void verifyFilesInZip() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                getClass().getResourceAsStream(fileUnpacker.getArchiveName())
        )) {
            ZipEntry entry;
            List<String> expectedFiles = new ArrayList<>(List.of("someNamePDF.pdf", "someNameCSV.csv", "someNameXLSX.xlsx"));
            List<String> actualFiles = new ArrayList<>();

            while ((entry = zis.getNextEntry()) != null) {
                actualFiles.add(entry.getName());
            }

            Collections.sort(expectedFiles);
            Collections.sort(actualFiles);

            assertEquals(expectedFiles, actualFiles);
        }
    }



    @Test
    void pdfReadTest() throws Exception {
        try (InputStream pdfFile = fileUnpacker.extractFileFromZip("someNamePDF.pdf")) {
            PDDocument document = PDDocument.load(pdfFile);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            assertThat(text).contains("someTextPDF");
        }}

    @Test
    void xlsxReadTest() throws Exception {
        try (InputStream xlsxFile = fileUnpacker.extractFileFromZip("someNameXLSX.xlsx")) {
            XLS xlsFile = new XLS(xlsxFile);
            Sheet sheet = xlsFile.excel.getSheetAt(0);
            Row row = sheet.getRow(11);
            assertThat(row).isNotNull();
            Cell cell = row.getCell(12);
            assertThat(cell).isNotNull();
            String actualTitle = cell.getStringCellValue();
            assertThat(actualTitle).isEqualTo("someTextXLSX");
        }
    }



    @Test
    void csvReadTest() throws Exception {
        try (InputStream csvFile = fileUnpacker.extractFileFromZip("someNameCSV.csv")) {
            CSVReader csvReader = new CSVReader(new InputStreamReader(csvFile));
            List<String[]> data = csvReader.readAll();
            String[] firstRow = data.get(0);
            assertThat(firstRow).isNotNull();
            String actualText = firstRow[0];
            assertThat(actualText).isEqualTo("someTextCSV");
        }
    }



    @Test
    void jsonReadTest() throws IOException {
        String json = "[\n" +
                "  {\n" +
                "    \"name\": \"Ivan\",\n" +
                "    \"secondName\": \"Ivanov\",\n" +
                "    \"extId\": \"6235423455345\",\n" +
                "    \"birthDate\": \"19.07.1995\",\n" +
                "    \"services\": {\n" +
                "      \"serviceExtId\": \"464352763\",\n" +
                "      \"serviceId\": \"234523\",\n" +
                "      \"main\": true,\n" +
                "      \"serviceName\": \"Internet\",\n" +
                "      \"dateStart\": \"2020-01-01T00:00:00+07:00\",\n" +
                "      \"dateEnd\": \"2074-01-01T00:00:00+07:00\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Peter\",\n" +
                "    \"secondName\": \"Petrov\",\n" +
                "    \"extId\": \"9568234235\",\n" +
                "    \"birthDate\": \"11.03.1999\",\n" +
                "    \"services\": {\n" +
                "      \"serviceExtId\": \"45876654\",\n" +
                "      \"serviceId\": \"432323523\",\n" +
                "      \"main\": false,\n" +
                "      \"serviceName\": \"Phone\",\n" +
                "      \"dateStart\": \"2010-01-01T00:00:00+07:00\",\n" +
                "      \"dateEnd\": \"2074-01-01T00:00:00+07:00\"\n" +
                "    }\n" +
                "  }\n" +
                "]";

        ObjectMapper objectMapper = new ObjectMapper();
        List<Client> people = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Client.class));

        assertEquals(2, people.size(), "The list should contain 2 persons");

        Client firstPerson = people.get(0);
        assertEquals("Ivan", firstPerson.getName(), "First person name should be 'Ivan'");
        assertEquals("Ivanov", firstPerson.getSecondName(), "First person second name should be 'Ivanov'");
        assertEquals("6235423455345", firstPerson.getExtId(), "First person extId should be '6235423455345'");
        assertEquals("19.07.1995", firstPerson.getBirthDate(), "First person birth date should be '19.07.1995'");

        assertThat(firstPerson.getServices().getServiceName()).isEqualTo("Internet");
        assertThat(firstPerson.getServices().getDateStart()).isEqualTo("2020-01-01T00:00:00+07:00");

        Client secondPerson = people.get(1);
        assertEquals("Peter", secondPerson.getName(), "Second person name should be 'Peter'");
        assertEquals("Petrov", secondPerson.getSecondName(), "Second person second name should be 'Petrov'");
        assertEquals("9568234235", secondPerson.getExtId(), "Second person extId should be '9568234235'");
        assertEquals("11.03.1999", secondPerson.getBirthDate(), "Second person birth date should be '11.03.1999'");

        assertThat(secondPerson.getServices().getServiceName()).isEqualTo("Phone");
        assertThat(secondPerson.getServices().getDateStart()).isEqualTo("2010-01-01T00:00:00+07:00");
    }
}
