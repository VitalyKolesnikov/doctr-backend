package ru.kvs.doctrspring;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class HexagonalArchitectureTest {

    private static final String ADAPTERS = "Adapters";
    private static final String OPERATIONS = "Operations";
    private static final String DOMAIN = "Domain";
    private static final String SECURITY = "Security";
    private static final String TESTS = "Tests";
    private static final String ENTRY_POINT = "EntryPoint";

    @Test
    @DisplayName("Ensure hexagonal architecture layers are respected")
    void hexagonal_architecture_layers_are_respected() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ru.kvs.doctrspring");

        Architectures.LayeredArchitecture architecture = layeredArchitecture()
                .consideringAllDependencies()
                .layer(ADAPTERS).definedBy("ru.kvs.doctrspring.adapters..")
                .layer(OPERATIONS).definedBy("ru.kvs.doctrspring.app..")
                .layer(DOMAIN).definedBy("ru.kvs.doctrspring.domain..")
                .layer(SECURITY).definedBy("ru.kvs.doctrspring.security..")
                .layer(TESTS).definedBy("ru.kvs.doctrspring.integrationtest..")
                .layer(ENTRY_POINT).definedBy("ru.kvs.doctrspring")
//                .whereLayer(ADAPTERS).mayOnlyBeAccessedByLayers(TESTS, ENTRY_POINT) // TODO: 10.09.2022 uncomment after adding mapstruct
                .whereLayer(OPERATIONS).mayOnlyBeAccessedByLayers(ADAPTERS, TESTS, ENTRY_POINT, SECURITY)
                .whereLayer(DOMAIN).mayOnlyBeAccessedByLayers(ADAPTERS, OPERATIONS, TESTS, ENTRY_POINT, SECURITY);

        architecture.check(importedClasses);
    }

}
