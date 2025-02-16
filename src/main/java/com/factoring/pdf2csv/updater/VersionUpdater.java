package com.factoring.pdf2csv.updater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class VersionUpdater {

    private static final String CURRENT_VERSION = "1.0.0"; // Versão atual da aplicação
    private static final String REPO_URL = "https://api.github.com/repos/seu-usuario/seu-repositorio/releases/latest";
    private static final String DOWNLOAD_URL = "https://github.com/seu-usuario/seu-repositorio/releases/download/{version}/seu-arquivo.jar";

    public boolean isUpdateAvailable() {
        try {
            String latestVersion = getLatestVersionFromRepo();
            return isNewerVersion(latestVersion, CURRENT_VERSION);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getLatestVersionFromRepo() throws Exception {
        URL url = new URL(REPO_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        String json = content.toString();
        return parseVersionFromJson(json);
    }

    private String parseVersionFromJson(String json) {
        // Simplificação para obter a versão a partir do JSON
        // Você pode usar uma biblioteca JSON como Jackson ou Gson para parsing mais robusto
        String versionKey = "\"tag_name\":\"";
        int startIndex = json.indexOf(versionKey) + versionKey.length();
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex);
    }

    private boolean isNewerVersion(String latestVersion, String currentVersion) {
        String[] latestParts = latestVersion.split("\\.");
        String[] currentParts = currentVersion.split("\\.");
        for (int i = 0; i < latestParts.length; i++) {
            int latestPart = Integer.parseInt(latestParts[i]);
            int currentPart = Integer.parseInt(currentParts[i]);
            if (latestPart > currentPart) {
                return true;
            } else if (latestPart < currentPart) {
                return false;
            }
        }
        return false;
    }

    public void updateApplication() {
        try {
            String latestVersion = getLatestVersionFromRepo();
            String downloadUrl = DOWNLOAD_URL.replace("{version}", latestVersion);

            // Baixar o novo arquivo JAR
            downloadFile(downloadUrl, "new_version.jar");

            // Substituir o arquivo JAR atual pelo novo
            Files.move(Paths.get("new_version.jar"), Paths.get("seu-arquivo.jar"), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Aplicação atualizada com sucesso para a versão " + latestVersion + ".");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar a aplicação.");
        }
    }

    private void downloadFile(String fileURL, String savePath) throws Exception {
        URL url = new URL(fileURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
    }

    public static void main(String[] args) {
        VersionUpdater updater = new VersionUpdater();
        if (updater.isUpdateAvailable()) {
            updater.updateApplication();
        } else {
            System.out.println("Você já está usando a versão mais recente.");
        }
    }
}
