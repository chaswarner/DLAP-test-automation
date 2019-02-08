package com.prft.cif.test;
        import com.prft.cif.test.util.HdfsUtil;
        import com.prft.cif.test.util.ResourceUtil;
        import com.prft.cif.test.util.WorkbookUtil;
        import cucumber.api.java.After;
        import cucumber.api.java.Before;
        import org.apache.commons.io.FileUtils;

        import java.io.File;
        import java.util.List;


public class SetupSteps  {

    ResourceUtil ru;
    WorkbookUtil wu;
    HdfsUtil hu;
    private static boolean dunit = false;
    private List<File> beforeOnboardingFilelist=null;

    public SetupSteps(WorkbookUtil wu, ResourceUtil ru, HdfsUtil hu) {
        this.ru = ru;
        this.wu=wu;
        this.hu=hu;
    }

    @Before
    public void setup() throws Exception {
        System.out.println("before inside of SetupSteps--->");
        ru.loadResources();
        wu.scanWorkbook();
        hu.initilizeHdfs();

        if (!dunit) {
            System.out.println("Data Ingestion --> dunit variable value:-->" + dunit);

            String[] extensions = new String[]{"xlsx"};
            beforeOnboardingFilelist = (List<File>) FileUtils.listFiles(new File(ru.getBaseStg()), extensions, true);

            System.out.println("Get base staging directory path-->"+ru.getBaseStg());
            System.out.println("List of file before onboarding--> " + beforeOnboardingFilelist.toString());
            for (File file : beforeOnboardingFilelist) {
                if (file.getParent().endsWith("publish")) {
                    System.out.println("COPYING file from publish stg to onboarding publish dir " + ru.getOnbpubStg() + " -->" + ru.getOnbDirPub());
                    FileUtils.copyFileToDirectory(new File(file.getPath()), new File(ru.getOnbDirPub()), false);
                } else {
                    System.out.println("COPYING file from curate stg to onboarding curate dir " + ru.getOnbCurStg() + " -->" + ru.getOnbDir());
                    FileUtils.copyFileToDirectory(new File(file.getPath()), new File(ru.getOnbDir()), false);
                }
            }
            dunit = true;
            Thread.sleep(30000);

        }
    }

    @After
    public void teardown() {
        System.out.println("afer inside of SetupSteps--->");
//            controller.teardownController();
    }
}

