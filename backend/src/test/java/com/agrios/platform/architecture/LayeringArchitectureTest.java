package com.agrios.platform.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

class LayeringArchitectureTest {
    @Test
    void domainPackagesMustNotDependOnWebOrPersistenceFrameworks() {
        var classes = new ClassFileImporter().importPackages("com.agrios.platform");

        noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("org.springframework.web..", "jakarta.persistence..")
                .check(classes);
    }
}
