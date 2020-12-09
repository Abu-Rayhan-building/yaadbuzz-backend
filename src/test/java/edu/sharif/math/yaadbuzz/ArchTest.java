package edu.sharif.math.yaadbuzz;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("edu.sharif.math.yaadbuzz");

        noClasses()
            .that()
                .resideInAnyPackage("edu.sharif.math.yaadbuzz.service..")
            .or()
                .resideInAnyPackage("edu.sharif.math.yaadbuzz.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..edu.sharif.math.yaadbuzz.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
