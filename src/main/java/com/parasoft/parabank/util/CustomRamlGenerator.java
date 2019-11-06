package com.parasoft.parabank.util;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com._8x8.cloud.swagger2raml.model.Api;
import com._8x8.cloud.swagger2raml.reader.SwaggerApiReader;
import com._8x8.cloud.swagger2raml.writer.ApiWriter;

@Component("customRamlGenerator")
public class CustomRamlGenerator implements ApplicationContextAware {

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CustomRamlGenerator.class);

    ApplicationContext applicationContext;

    protected CustomRamlGenerator() {
        super();
        // TODO Auto-generated constructor stub
    }

    public File generateFromSwaggerUrl(final String url, final String outputFileName) {
        final Api api = new SwaggerApiReader().readFromUrl(url);

        final File outputFile = prepareOutputFile(outputFileName);

        final ApiWriter apiW = new ApiWriter();
        //apiW.setFile(outputFile);
        //apiW.setProperty("file", outputFile);
        apiW.writeApi(api);

        return outputFile;
    }

    public void generateRaml() {
        final String urlString =
            String.format("http://%1$s%2$s", Util.getHostPortString(), "/parabank/services/bank/swagger.json");
        final String currPath = Util.getCurrentPath(getApplicationContext());
        final String outputFileFolder = String.format(Constants.RAML_PATH_FMT, currPath, "");
        final String outputFileName = String.format(Constants.RAML_PATH_FMT, currPath, "raml.api");
        try {
            Util.prepareFolder(outputFileFolder);
            generateFromSwaggerUrl(urlString, outputFileName);
        } catch (final IOException ex1) {
            log.error("caught {} Error : ", ex1.getClass().getSimpleName() //$NON-NLS-1${0xD}
            , ex1);
        } catch (final Exception ex) {
            log.error("caught {} Error : ", ex.getClass().getSimpleName() //$NON-NLS-1${0xD}
            , ex);
        }

    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the applicationContext property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 28, 2015</DD>
     * </DL>
     *
     * @return the value of applicationContext field
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public File prepareOutputFile(final String outputFileName) {
        final File outputFile = new File(outputFileName);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        return outputFile;
    }

    @Override
    public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException {
        applicationContext = aApplicationContext;

    }

}
