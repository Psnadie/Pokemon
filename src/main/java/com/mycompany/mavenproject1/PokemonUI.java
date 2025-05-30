package com.mycompany.mavenproject1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import com.google.gson.Gson;
import java.net.HttpURLConnection;

public class PokemonUI extends JFrame {
    private JTextField searchField;
    private JTextArea infoArea;
    private JLabel spriteLabel;
    private JPanel statsPanel;
    private JPanel typesPanel;
    private JPanel abilitiesPanel;
    private JPanel speciesPanel;
    private JPanel variantsPanel;
    private JLabel errorLabel;
    private JScrollPane scrollPane;
    
    // Sprite navigation state
    private java.util.List<String> spriteUrls;
    private java.util.List<String> spriteLabels;
    private int currentSpriteIndex;
    
    // Current Pokemon
    private Pokemon currentPokemon;
    
    public PokemonUI() {
        // Set up the main frame
        setTitle("Pokédex");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchField = new JTextField();
        JButton searchButton = new JButton("Buscar");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Create info panel with scroll
        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        
        // Left panel for sprite and navigation
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        
        // Sprite panel
        JPanel spritePanel = new JPanel(new BorderLayout());
        spriteLabel = new JLabel();
        spriteLabel.setPreferredSize(new Dimension(200, 200));
        spriteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        spriteLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        spritePanel.add(spriteLabel, BorderLayout.CENTER);
        
        // Navigation panel
        JPanel navigationPanel = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("◀ Anterior");
        JButton nextButton = new JButton("Siguiente ▶");
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        
        // Add sprite and navigation to left panel
        leftPanel.add(spritePanel, BorderLayout.CENTER);
        leftPanel.add(navigationPanel, BorderLayout.SOUTH);
        
        // Create scrollable center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        // Types panel
        typesPanel = new JPanel();
        typesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        typesPanel.setBorder(BorderFactory.createTitledBorder("Tipos"));
        
        // Abilities panel
        abilitiesPanel = new JPanel();
        abilitiesPanel.setLayout(new BoxLayout(abilitiesPanel, BoxLayout.Y_AXIS));
        abilitiesPanel.setBorder(BorderFactory.createTitledBorder("Habilidades"));
        
        // Stats panel
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        
        // Species panel
        speciesPanel = new JPanel();
        speciesPanel.setLayout(new BoxLayout(speciesPanel, BoxLayout.Y_AXIS));
        speciesPanel.setBorder(BorderFactory.createTitledBorder("Información de Especie"));
        
        // Variants panel
        variantsPanel = new JPanel();
        variantsPanel.setLayout(new BoxLayout(variantsPanel, BoxLayout.Y_AXIS));
        variantsPanel.setBorder(BorderFactory.createTitledBorder("Formas y Variantes"));
        
        // Error label
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add components to center panel
        centerPanel.add(typesPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(abilitiesPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(statsPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(speciesPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(variantsPanel);
        
        // Add scroll pane
        scrollPane = new JScrollPane(centerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Add panels to info panel
        infoPanel.add(leftPanel, BorderLayout.WEST);
        infoPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add all components to main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(errorLabel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        searchButton.addActionListener(this::searchPokemon);
        searchField.addActionListener(this::searchPokemon);
        prevButton.addActionListener(e -> showPreviousSprite());
        nextButton.addActionListener(e -> showNextSprite());
    }
    
    private void searchPokemon(ActionEvent e) {
        String pokemonName = searchField.getText().toLowerCase().trim();
        if (pokemonName.isEmpty()) {
            showError("Por favor, ingresa un nombre de pokemon válido.");
            return;
        }
        
        try {
            // Clear previous error
            errorLabel.setText("");
            
            // Create URL and connection
            String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonName;
            var apiConnection = PruebaAPI.fetchApiResponse(url);
            
            if (apiConnection.getResponseCode() != 200) {
                showError("Pokemon no encontrado o problema de conexión.");
                return;
            }
            
            // Get Pokemon data
            String jsonResponse = PruebaAPI.readApiResponse(apiConnection);
            Pokemon pokemon = new Gson().fromJson(jsonResponse, Pokemon.class);
            
            // Update UI with Pokemon data
            updatePokemonInfo(pokemon);
            
        } catch (IOException ex) {
            showError("Error: " + ex.getMessage());
        }
    }
    
    private void updatePokemonInfo(Pokemon pokemon) {
        // Store the current Pokemon
        this.currentPokemon = pokemon;
        
        // Initialize sprite navigation
        initializeSpriteNavigation(pokemon.getSprites());
        
        // Show first sprite
        showCurrentSprite();
        
        // Clear all panels
        typesPanel.removeAll();
        abilitiesPanel.removeAll();
        statsPanel.removeAll();
        speciesPanel.removeAll();
        variantsPanel.removeAll();
        
        // Update types
        for (PokemonType type : pokemon.getTypes()) {
            JLabel typeLabel = new JLabel(type.getType().getName().toUpperCase());
            typeLabel.setOpaque(true);
            typeLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
            typeLabel.setBackground(getTypeColor(type.getType().getName()));
            typeLabel.setForeground(Color.WHITE);
            typesPanel.add(typeLabel);
        }
        
        // Update abilities
        for (PokemonAbility ability : pokemon.getAbilities()) {
            String abilityText = ability.getAbility().getName();
            if (ability.isHidden()) {
                abilityText += " (Oculta)";
            }
            JLabel abilityLabel = new JLabel(abilityText);
            abilityLabel.setBorder(new EmptyBorder(2, 5, 2, 5));
            abilitiesPanel.add(abilityLabel);
        }
        
        // Update stats
        for (PokemonStat stat : pokemon.getStats()) {
            String statName = translateStatName(stat.getStat().getName());
            JPanel statRow = new JPanel(new BorderLayout(10, 0));
            JLabel nameLabel = new JLabel(statName);
            JProgressBar statBar = new JProgressBar(0, 255);
            statBar.setValue(stat.getBaseStat());
            statBar.setStringPainted(true);
            statBar.setString(String.valueOf(stat.getBaseStat()));
            statRow.add(nameLabel, BorderLayout.WEST);
            statRow.add(statBar, BorderLayout.CENTER);
            statsPanel.add(statRow);
            statsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        // Fetch and update species information
        updateSpeciesInfo(pokemon.getSpecies());
        
        // Refresh the UI
        revalidate();
        repaint();
        
        // Reset scroll position
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });
    }
    
    private void updateSpeciesInfo(PokemonSpecies speciesRef) {
        if (speciesRef != null) {
            try {
                HttpURLConnection speciesConnection = PruebaAPI.fetchApiResponse(speciesRef.getUrl());
                if (speciesConnection.getResponseCode() == 200) {
                    String speciesJson = PruebaAPI.readApiResponse(speciesConnection);
                    Species speciesDetails = new Gson().fromJson(speciesJson, Species.class);
                    
                    // Create species info panel
                    speciesPanel.removeAll();
                    
                    // Basic characteristics
                    addSpeciesField("Categoría", findEnglishGenus(speciesDetails.getGenera()));
                    addSpeciesField("Ratio de Captura", String.valueOf(speciesDetails.getCaptureRate()));
                    addSpeciesField("Felicidad Base", String.valueOf(speciesDetails.getBaseHappiness()));
                    
                    // Special flags
                    if (speciesDetails.isBaby()) addSpeciesField("Tipo", "Pokémon Bebé");
                    if (speciesDetails.isLegendary()) addSpeciesField("Tipo", "Pokémon Legendario");
                    if (speciesDetails.isMythical()) addSpeciesField("Tipo", "Pokémon Mítico");
                    
                    // Physical characteristics
                    if (speciesDetails.getColor() != null) {
                        addSpeciesField("Color", speciesDetails.getColor().getName());
                    }
                    if (speciesDetails.getShape() != null) {
                        addSpeciesField("Forma", speciesDetails.getShape().getName());
                    }
                    
                    // Breeding information
                    addSpeciesField("Género", getGenderRateDescription(speciesDetails.getGenderRate()));
                    addSpeciesField("Ciclos de Eclosión", speciesDetails.getHatchCounter() + " ciclos");
                    
                    if (speciesDetails.getEggGroups() != null && !speciesDetails.getEggGroups().isEmpty()) {
                        addSpeciesField("Grupos Huevo", getEggGroupsString(speciesDetails.getEggGroups()));
                    }
                    
                    // Evolution information
                    if (speciesDetails.getEvolvesFromSpecies() != null) {
                        addSpeciesField("Evoluciona de", speciesDetails.getEvolvesFromSpecies().getName());
                    }
                    
                    // Description
                    String description = findEnglishFlavorText(speciesDetails.getFlavorTextEntries());
                    if (description != null) {
                        addSpeciesField("Descripción", description);
                    }
                    
                    // Update variants panel
                    updateVariantsPanel(speciesDetails.getVarieties());
                }
            } catch (IOException e) {
                addSpeciesField("Error", "Error al cargar información de especie: " + e.getMessage());
            }
        }
    }
    
    private void updateVariantsPanel(java.util.List<Species.PokemonVariety> varieties) {
        if (varieties != null && !varieties.isEmpty()) {
            variantsPanel.removeAll();
            
            for (Species.PokemonVariety variety : varieties) {
                try {
                    HttpURLConnection variantConnection = PruebaAPI.fetchApiResponse(variety.getPokemon().getUrl());
                    if (variantConnection.getResponseCode() == 200) {
                        String variantJson = PruebaAPI.readApiResponse(variantConnection);
                        Pokemon variantPokemon = new Gson().fromJson(variantJson, Pokemon.class);
                        
                        // Add variant sprites to navigation
                        String variantName = variety.getPokemon().getName();
                        if (!variety.isDefault()) {  // Skip default form as it's already included
                            addVariantSprites(variantPokemon, capitalizeFirstLetter(variantName));
                        }
                        
                        // Create panel for this variant
                        JPanel variantPanel = new JPanel();
                        variantPanel.setLayout(new BoxLayout(variantPanel, BoxLayout.Y_AXIS));
                        variantPanel.setBorder(BorderFactory.createTitledBorder(
                            variety.getPokemon().getName().toUpperCase() + 
                            (variety.isDefault() ? " (Forma Principal)" : "")
                        ));
                        
                        // Add stats
                        JPanel statsPanel = new JPanel(new GridLayout(0, 1, 2, 2));
                        for (PokemonStat stat : variantPokemon.getStats()) {
                            JPanel statRow = new JPanel(new BorderLayout(10, 0));
                            JLabel nameLabel = new JLabel(translateStatName(stat.getStat().getName()));
                            JProgressBar statBar = new JProgressBar(0, 255);
                            statBar.setValue(stat.getBaseStat());
                            statBar.setStringPainted(true);
                            statBar.setString(String.valueOf(stat.getBaseStat()));
                            statRow.add(nameLabel, BorderLayout.WEST);
                            statRow.add(statBar, BorderLayout.CENTER);
                            statsPanel.add(statRow);
                        }
                        variantPanel.add(statsPanel);
                        
                        // Add types
                        JPanel typesRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        typesRow.add(new JLabel("Tipos: " + variantPokemon.getTypesAsString()));
                        variantPanel.add(typesRow);
                        
                        // Add abilities
                        JPanel abilitiesRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        abilitiesRow.add(new JLabel("Habilidades: " + variantPokemon.getAbilitiesAsString()));
                        variantPanel.add(abilitiesRow);
                        
                        // Add height and weight
                        JPanel sizeRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        sizeRow.add(new JLabel(String.format("Altura: %.1f m, Peso: %.1f kg", 
                            variantPokemon.getHeight() / 10.0, 
                            variantPokemon.getWeight() / 10.0)));
                        variantPanel.add(sizeRow);
                        
                        variantsPanel.add(variantPanel);
                        variantsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                } catch (IOException e) {
                    JLabel errorLabel = new JLabel("Error al cargar variante: " + e.getMessage());
                    errorLabel.setForeground(Color.RED);
                    variantsPanel.add(errorLabel);
                }
            }
        }
    }
    
    private void addSpeciesField(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.add(new JLabel(label + ":"), BorderLayout.WEST);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setBorder(new EmptyBorder(2, 5, 2, 5));
        row.add(valueLabel, BorderLayout.CENTER);
        speciesPanel.add(row);
        speciesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    // Helper methods from PruebaAPI
    private String findEnglishGenus(java.util.List<Species.Genus> genera) {
        if (genera != null) {
            for (Species.Genus genus : genera) {
                if (genus.getLanguage() != null && 
                    genus.getLanguage().getName().equals("en")) {
                    return genus.getGenus();
                }
            }
        }
        return "Desconocido";
    }
    
    private String findEnglishFlavorText(java.util.List<Species.FlavorTextEntry> entries) {
        if (entries != null) {
            for (Species.FlavorTextEntry entry : entries) {
                if (entry.getLanguage() != null && 
                    entry.getLanguage().getName().equals("en")) {
                    return entry.getFlavorText()
                        .replace("\n", " ")
                        .replace("\f", " ");
                }
            }
        }
        return null;
    }
    
    private String getGenderRateDescription(int genderRate) {
        if (genderRate == -1) return "Sin Género";
        double femalePercentage = (genderRate / 8.0) * 100;
        double malePercentage = 100 - femalePercentage;
        return String.format("%.1f%% Macho, %.1f%% Hembra", malePercentage, femalePercentage);
    }
    
    private String getEggGroupsString(java.util.List<Species.NamedAPIResource> eggGroups) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < eggGroups.size(); i++) {
            sb.append(capitalizeFirstLetter(eggGroups.get(i).getName()));
            if (i < eggGroups.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
    
    private void initializeSpriteNavigation(PokemonSprites mainSprites) {
        spriteUrls = new java.util.ArrayList<>();
        spriteLabels = new java.util.ArrayList<>();
        
        // Add main Pokemon sprites
        addSpriteIfNotNull(mainSprites.getFrontDefault(), "Frontal Normal");
        addSpriteIfNotNull(mainSprites.getBackDefault(), "Trasero Normal");
        addSpriteIfNotNull(mainSprites.getFrontShiny(), "Frontal Shiny");
        addSpriteIfNotNull(mainSprites.getBackShiny(), "Trasero Shiny");
        
        currentSpriteIndex = 0;
    }
    
    private void addSpriteIfNotNull(String url, String label) {
        if (url != null && !url.isEmpty()) {
            spriteUrls.add(url);
            spriteLabels.add(label);
        }
    }
    
    private void addVariantSprites(Pokemon variantPokemon, String variantName) {
        PokemonSprites sprites = variantPokemon.getSprites();
        if (sprites != null) {
            addSpriteIfNotNull(sprites.getFrontDefault(), "Frontal " + variantName);
            addSpriteIfNotNull(sprites.getBackDefault(), "Trasero " + variantName);
            addSpriteIfNotNull(sprites.getFrontShiny(), "Frontal Shiny " + variantName);
            addSpriteIfNotNull(sprites.getBackShiny(), "Trasero Shiny " + variantName);
        }
    }
    
    private void showCurrentSprite() {
        if (spriteUrls == null || spriteUrls.isEmpty()) {
            spriteLabel.setIcon(null);
            spriteLabel.setText("No hay sprites disponibles");
            return;
        }
        
        try {
            // Load and display the current sprite
            String currentUrl = spriteUrls.get(currentSpriteIndex);
            String currentLabel = spriteLabels.get(currentSpriteIndex);
            
            URL url = new URL(currentUrl);
            java.awt.image.BufferedImage bufferedImage = ImageIO.read(url);
            
            if (bufferedImage != null) {
                int targetSize = 200;
                Image scaledImage = bufferedImage.getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImage);
                spriteLabel.setIcon(icon);
                spriteLabel.setText(currentLabel);
                spriteLabel.setHorizontalTextPosition(JLabel.CENTER);
                spriteLabel.setVerticalTextPosition(JLabel.BOTTOM);
            }
        } catch (Exception e) {
            System.err.println("Error loading sprite: " + e.getMessage());
            spriteLabel.setIcon(null);
            spriteLabel.setText("Error loading sprite");
        }
    }
    
    private void showNextSprite() {
        if (spriteUrls != null && !spriteUrls.isEmpty()) {
            currentSpriteIndex = (currentSpriteIndex + 1) % spriteUrls.size();
            showCurrentSprite();
        }
    }
    
    private void showPreviousSprite() {
        if (spriteUrls != null && !spriteUrls.isEmpty()) {
            currentSpriteIndex = (currentSpriteIndex - 1 + spriteUrls.size()) % spriteUrls.size();
            showCurrentSprite();
        }
    }
    
    private String translateStatName(String statName) {
        return switch (statName) {
            case "hp" -> "PS (Puntos de Salud)";
            case "attack" -> "Ataque";
            case "defense" -> "Defensa";
            case "special-attack" -> "Ataque Especial";
            case "special-defense" -> "Defensa Especial";
            case "speed" -> "Velocidad";
            default -> statName;
        };
    }
    
    private Color getTypeColor(String type) {
        return switch (type.toLowerCase()) {
            case "normal" -> new Color(189, 189, 189);
            case "fire" -> new Color(240, 128, 48);
            case "water" -> new Color(104, 144, 240);
            case "electric" -> new Color(248, 208, 48);
            case "grass" -> new Color(120, 200, 80);
            case "ice" -> new Color(152, 216, 216);
            case "fighting" -> new Color(255, 112, 67);
            case "poison" -> new Color(160, 64, 160);
            case "ground" -> new Color(224, 192, 104);
            case "flying" -> new Color(130, 177, 255);
            case "psychic" -> new Color(248, 88, 136);
            case "bug" -> new Color(168, 184, 32);
            case "rock" -> new Color(184, 160, 56);
            case "ghost" -> new Color(112, 88, 152);
            case "dragon" -> new Color(112, 56, 248);
            case "dark" -> new Color(112, 88, 72);
            case "steel" -> new Color(184, 184, 208);
            case "fairy" -> new Color(238, 153, 172);
            default -> Color.GRAY;
        };
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        // Clear previous Pokemon info
        spriteLabel.setIcon(null);
        typesPanel.removeAll();
        abilitiesPanel.removeAll();
        statsPanel.removeAll();
        speciesPanel.removeAll();
        variantsPanel.removeAll();
        revalidate();
        repaint();
    }
    
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show UI
        SwingUtilities.invokeLater(() -> {
            new PokemonUI().setVisible(true);
        });
    }
} 