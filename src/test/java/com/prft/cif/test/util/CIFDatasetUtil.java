package com.prft.cif.test.util;

import com.prft.cif.test.metadata.CIFDataset;
import com.prft.cif.test.metadata.CIFSchema;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Anant Gowerdhan
 * @version 1.0
 */
public class CIFDatasetUtil {

    private CIFDatasetUtil(){}
    /**
     * @param dataset
     * @return
     * @throws NullPointerException
     */
    public static String getFileLocation(CIFDataset dataset) throws NullPointerException {
        checkEssentialsForNull(dataset);

        return getEnvironmentFolderPrefix() + "/" + dataset.getZone() + "/" + dataset.getNamespace() + "/" + dataset.getName();
    }

    public static String getAuditLocation(CIFDataset dataset) throws NullPointerException {
        checkEssentialsForNull(dataset);
        return getEnvironmentFolderPrefix() + "/" + dataset.getZone() + "/audit" + "/" + dataset.getNamespace() + "/" + dataset.getName();
    }

    public static String getCopybookLocation() throws NullPointerException {
        return getEnvironmentFolderPrefix() + "/cif/copybooks/";
    }

    public static String getMultirecordFileLocation(CIFDataset dataset) throws NullPointerException {
        if (StringUtils.isBlank(dataset.getZone()))
            throw new NullPointerException("Zone can not be null or empty.");
        if (StringUtils.isBlank(dataset.getNamespace()))
            throw new NullPointerException("Namespace can not be null or empty.");
        if (StringUtils.isBlank(dataset.getName()))
            throw new NullPointerException("Name can not be null or empty.");

        return getEnvironmentFolderPrefix()+"/" + dataset.getZone() + "/" + dataset.getSourceSystemCode() + "/" + "multirecord";
    }


    public static String getCompactionFileLocation(CIFDataset dataset) throws NullPointerException {
        checkEssentialsForNull(dataset);

        return getEnvironmentFolderPrefix() + "/" + dataset.getZone() + "/_compaction/" + dataset.getNamespace() + "/" + dataset.getName();
    }


    public static String getSCDFileLocation(CIFDataset dataset) throws NullPointerException {
        checkEssentialsForNull(dataset);

        return getEnvironmentFolderPrefix() + "/" + dataset.getZone() + "/" + dataset.getNamespace() + "/" + dataset.getName() + "_scd";
    }

    /**
     * @param dataset
     * @return
     */
    public static String getHiveTableName(CIFDataset dataset) {
        checkEssentialsForNull(dataset);

        return getEnvironmentDatabasePrefix() + dataset.getZone() + "_" + dataset.getNamespace() + "." + dataset.getName();
    }

    /**
     * @param dataset
     * @return
     */
    public static String getSCDHiveTableName(CIFDataset dataset) {
        checkEssentialsForNull(dataset);
        return getEnvironmentDatabasePrefix() + dataset.getZone() + "_" + dataset.getNamespace() + "." + dataset.getName() + "_scd";
    }

    /**
     * @param dataset
     * @return
     */
    public static List<CIFSchema> getNonSCDSchema(CIFDataset dataset) {
        Set<String> excludeList = new HashSet<String>();
        excludeList.add("cflag_");
        excludeList.add("dflag_");
        List<CIFSchema> schemas = new ArrayList<CIFSchema>();
        dataset.getSchemas().forEach(schema -> {
            if (!excludeList.contains(schema.getName().trim())) {
                schemas.add(schema);
            }
        });

        return schemas;
    }

    /**
     * @param dataset
     * @return
     */
    public static String getHiveViewName(CIFDataset dataset) {
        checkEssentialsForNull(dataset);

        return getEnvironmentDatabasePrefix() + dataset.getZone() + "_" + dataset.getNamespace() + ".v_" + dataset.getName();

    }

    public static String getHiveSecureViewName(CIFDataset dataset) {
        checkEssentialsForNull(dataset);
        return getEnvironmentDatabasePrefix() + dataset.getZone() + "_" + dataset.getNamespace() + ".vs_" + dataset.getName();

    }

    /**
     * @param dataset
     * @return
     * @throws NullPointerException
     */
    public static String getRawHbaseRowKey(CIFDataset dataset) throws NullPointerException {
        if (StringUtils.isBlank(dataset.getZone()))
            throw new NullPointerException("Zone can not be null or empty.");
        if (StringUtils.isBlank(dataset.getSourceSystemCode()))
            throw new NullPointerException("Source System Code can not be null or empty.");
        if (StringUtils.isBlank(dataset.getName()))
            throw new NullPointerException("Name can not be null or empty.");
        if (StringUtils.isBlank(dataset.getVersion()))
            throw new NullPointerException("Version can not be null or empty.");

        return "raw" + "_" + dataset.getSourceSystemCode() + "." + dataset.getName() + "." + dataset.getVersion();
    }

    /**
     * @param dataset
     * @return
     * @throws NullPointerException
     */
    public static String getCurateHbaseRowKey(CIFDataset dataset) throws NullPointerException {
        if (StringUtils.isBlank(dataset.getZone()))
            throw new NullPointerException("Zone can not be null or empty.");
        if (StringUtils.isBlank(dataset.getBusinessDomain()))
            throw new NullPointerException("Source System Code can not be null or empty.");
        if (StringUtils.isBlank(dataset.getName()))
            throw new NullPointerException("Name can not be null or empty.");
        if (StringUtils.isBlank(dataset.getVersion()))
            throw new NullPointerException("Version can not be null or empty.");

        return "curate" + "_" + dataset.getBusinessDomain() + "." + dataset.getName() + "." + dataset.getVersion();
    }

    /**
     * @param dataset
     * @return
     * @throws NullPointerException
     */
    public static String getPublishHbaseRowKey(CIFDataset dataset) throws NullPointerException {
        if (StringUtils.isBlank(dataset.getZone()))
            throw new NullPointerException("Zone can not be null or empty.");
        if (StringUtils.isBlank(dataset.getBusinessDomain()))
            throw new NullPointerException("Source System Code can not be null or empty.");
        if (StringUtils.isBlank(dataset.getName()))
            throw new NullPointerException("Name can not be null or empty.");
        if (StringUtils.isBlank(dataset.getVersion()))
            throw new NullPointerException("Version can not be null or empty.");

        return "publish" + "_" + dataset.getBusinessDomain() + "." + dataset.getName() + "." + dataset.getVersion();
    }

    public static String getRowKey(CIFDataset dataset){
        if(dataset.getZone().equalsIgnoreCase("raw"))
            return com.prft.cif.test.util.CIFDatasetUtil.getRawHbaseRowKey(dataset);
        else if(dataset.getZone().equalsIgnoreCase("curate"))
            return com.prft.cif.test.util.CIFDatasetUtil.getCurateHbaseRowKey(dataset);
        else if(dataset.getZone().equalsIgnoreCase("publish"))
            return com.prft.cif.test.util.CIFDatasetUtil.getPublishHbaseRowKey(dataset);
        else
            return null;
    }

    public static String getEnvironmentFolderPrefix() {
        String env = System.getenv("cif_env");
        if (StringUtils.isNotBlank(env))
            return "/" + env;
        return "";
    }

    public static String getEnvironmentDatabasePrefix() {
        String env = System.getenv("cif_env");
        if (StringUtils.isNotBlank(env))
            return env + "_";
        return "";
    }

    public static String getDatabaseName(CIFDataset dataset) {
        if (StringUtils.isBlank(dataset.getZone()))
            throw new NullPointerException("Zone can not be null or empty.");
        if (StringUtils.isBlank(dataset.getNamespace()))
            throw new NullPointerException("Namespace can not be null or empty.");
        return getEnvironmentDatabasePrefix() + dataset.getZone() + "_" + dataset.getNamespace();
    }


    public static List<String> getRoles(CIFDataset dataset) {
        List<String> roles = new ArrayList<>();
        roles.add(getRole(dataset)); //Normal Role
        if (dataset.isSensitiveData()) {
            roles.add(getSecureRole(dataset)); //Secure Role
        }
        return roles;
    }

    public static String getRole(CIFDataset dataset) {
        return getEnvironmentDatabasePrefix() + dataset.getBusinessDomain() + "_role";
    }

    public static String getSecureRole(CIFDataset dataset) {
        return getEnvironmentDatabasePrefix() + dataset.getBusinessDomain() + "_sec_role";
    }


    private static void checkEssentialsForNull(CIFDataset dataset) throws NullPointerException {
        if (StringUtils.isBlank(dataset.getZone()))
            throw new NullPointerException("Zone can not be null or empty.");
        if (StringUtils.isBlank(dataset.getNamespace()))
            throw new NullPointerException("Namespace can not be null or empty.");
        if (StringUtils.isBlank(dataset.getName()))
            throw new NullPointerException("Name can not be null or empty.");
    }
}
