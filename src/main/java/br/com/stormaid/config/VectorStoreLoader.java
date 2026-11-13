package br.com.stormaid.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VectorStoreLoader {

    private static final Logger log = LoggerFactory.getLogger(VectorStoreLoader.class);
    private final VectorStore vectorStore;
    private final ResourceLoader resourceLoader;

    public VectorStoreLoader(VectorStore vectorStore, ResourceLoader resourceLoader) {
        this.vectorStore = vectorStore;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void loadKnowledgeBase() {
        try {
            Resource resource = resourceLoader.getResource("classpath:knowledge/guia-emergencias.md");
            if (resource.exists()) {
                TextReader textReader = new TextReader(resource);
                List<Document> documents = textReader.get();

                TokenTextSplitter textSplitter = new TokenTextSplitter();
                List<Document> splitDocuments = textSplitter.apply(documents);

                vectorStore.add(splitDocuments);
                log.info("Base de conhecimento RAG carregada com sucesso! Total de chunks: {}", splitDocuments.size());
            } else {
                log.warn("Ficheiro guia-emergencias.md não encontrado na pasta knowledge.");
            }
        } catch (Exception e) {
            log.error("Erro ao carregar a base de conhecimento para o VectorStore: {}", e.getMessage());
        }
    }
}