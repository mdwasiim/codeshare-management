package com.codeshare.airline.tools.ssim.master;

import com.codeshare.airline.tools.ssim.master.db.MasterDataJdbcImporter;
import com.codeshare.airline.tools.ssim.master.extract.SsimMasterExtractor;
import com.codeshare.airline.tools.ssim.master.model.ImportConfig;
import com.codeshare.airline.tools.ssim.master.model.MasterCodeSet;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class SsimMasterImporterApplication {

    private SsimMasterImporterApplication() {
    }

    public static void main(String[] args) throws Exception {
        ImportConfig config = ImportConfig.from(args);
        if (config.ssimFile() == null) {
            throw new IllegalArgumentException("Missing --ssim-file or ssim.file in config.");
        }

        MasterCodeSet codes = new SsimMasterExtractor().extract(Path.of(config.ssimFile()));
        System.out.println(codes.summary());

        if (config.dryRun()) {
            System.out.println("Dry-run enabled. No database changes were made.");
            return;
        }

        Properties props = new Properties();
        props.setProperty("user", config.dbUser());
        props.setProperty("password", config.dbPassword());

        try (Connection connection = DriverManager.getConnection(config.dbUrl(), props)) {
            connection.setAutoCommit(false);
            MasterDataJdbcImporter importer = new MasterDataJdbcImporter(connection, config);
            importer.importAll(codes);
            connection.commit();
        }
    }
}
