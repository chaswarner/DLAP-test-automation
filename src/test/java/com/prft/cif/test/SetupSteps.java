package com.prft.cif.test;
import com.prft.cif.test.util.ResourceUtil;
import cucumber.api.java.After;
import cucumber.api.java.Before;


    public class SetupSteps  {

        ResourceUtil ru;


        public SetupSteps(ResourceUtil ru) {
            this.ru = ru;

        }

        @Before
        public void setup() throws Exception {
            System.out.println("before inside of SetupSteps--->");
            ru.loadResources();
        }

        @After
        public void teardown() {
            System.out.println("afer inside of SetupSteps--->");
//            controller.teardownController();
        }
    }

