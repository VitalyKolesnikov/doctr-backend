package ru.kvs.doctrspring;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class HexagonalArchitectureTest {

    private static final String LAYER_ADAPTERS = "Adapters";
    private static final String LAYER_OPERATIONS = "Operations";
    private static final String LAYER_DOMAIN = "Domain";
    private static final String LAYER_SECURITY = "Security";
    private static final String LAYER_TESTS = "Tests";
    private static final String LAYER_ENTRY_POINT = "EntryPoint";

    @Test
    @DisplayName("Ensure hexagonal architecture layers are respected")
    void hexagonal_architecture_layers_are_respected() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ru.kvs.doctrspring");

        Architectures.LayeredArchitecture architecture = layeredArchitecture()
                .consideringAllDependencies()
                .layer(LAYER_ADAPTERS).definedBy("ru.kvs.doctrspring.adapters..")
                .layer(LAYER_OPERATIONS).definedBy("ru.kvs.doctrspring.app..")
                .layer(LAYER_DOMAIN).definedBy("ru.kvs.doctrspring.domain..")
                .layer(LAYER_SECURITY).definedBy("ru.kvs.doctrspring.security..")
                .layer(LAYER_TESTS).definedBy("ru.kvs.doctrspring.integrationtest..")
                .layer(LAYER_ENTRY_POINT).definedBy("ru.kvs.doctrspring")
//                .whereLayer(LAYER_ADAPTERS).mayOnlyBeAccessedByLayers(LAYER_TESTS, LAYER_ENTRY_POINT) // TODO: 10.09.2022 uncomment after adding mapstruct
                .whereLayer(LAYER_OPERATIONS).mayOnlyBeAccessedByLayers(LAYER_ADAPTERS, LAYER_TESTS, LAYER_ENTRY_POINT, LAYER_SECURITY)
                .whereLayer(LAYER_DOMAIN).mayOnlyBeAccessedByLayers(LAYER_ADAPTERS, LAYER_OPERATIONS, LAYER_TESTS, LAYER_ENTRY_POINT, LAYER_SECURITY);

        architecture.check(importedClasses);
    }

}
