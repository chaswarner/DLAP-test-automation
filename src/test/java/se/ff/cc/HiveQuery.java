package se.ff.cc;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.prft.cif.conversion.MetadataWorkbook;
import com.prft.cif.guice.inject.CIFInjector;
import com.prft.cif.metadata.CIFDataset;
import com.prft.cif.metadata.CIFSchema;
import com.prft.cif.process.CIFService;
import com.prft.cif.repo.CIFHive;
import com.prft.cif.repo.query.CIFHiveQuery;
import org.codehaus.jettison.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HiveQuery extends CIFService {
    @Inject
    @Named("cluster.name")
    private String clusterName;

    @Inject
    @Named("dataset.namespace.raw")
    private String rawZone;

    @Inject
    @Named("dataset.namespace.curate")
    private String curatedZone;

    @Inject
    @Named("dataset.namespace.publish")
    private String publishZone;

    @Inject
    @Named("onboarding.dir")
    private String onboardingDir;

    private Boolean flumeRefresh = false;

    @Inject
    private CIFHive hive;

    @Inject
    private CIFHiveQuery hiveQuery;

    private static final String query = "SELECT * FROM test_curate_fin.test_cash_detail";

    List<CIFSchema> schema;


    @Override
    public void run() {
        hive.execute(query);
    }

}

