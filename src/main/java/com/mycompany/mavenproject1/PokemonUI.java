package com.mycompany.mavenproject1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import com.google.gson.Gson;

public class PokemonUI extends JFrame {
    private JTextField searchField;
    private JTextArea infoArea;
    private JLabel spriteLabel;
    private JPanel statsPanel;
    private JPanel typesPanel;
    private JPanel abilitiesPanel;
    private JLabel errorLabel;
    
    // Sprite navigation state
    private String[] spriteUrls;
    private String[] spriteLabels;
    private int currentSpriteIndex;
    
    // Current Pokemon
    private Pokemon currentPokemon;
    
    public PokemonUI() {
        // Set up the main frame
        setTitle("Pokédex");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
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
        
        // Create info panel
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
        
        // Center panel for Pokemon info
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
        
        // Add panels to info panel
        infoPanel.add(leftPanel, BorderLayout.WEST);
        infoPanel.add(centerPanel, BorderLayout.CENTER);
        
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
        
        // Update types
        typesPanel.removeAll();
        for (PokemonType type : pokemon.getTypes()) {
            JLabel typeLabel = new JLabel(type.getType().getName().toUpperCase());
            typeLabel.setOpaque(true);
            typeLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
            typeLabel.setBackground(getTypeColor(type.getType().getName()));
            typeLabel.setForeground(Color.WHITE);
            typesPanel.add(typeLabel);
        }
        
        // Update abilities
        abilitiesPanel.removeAll();
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
        statsPanel.removeAll();
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
        
        // Refresh the UI
        revalidate();
        repaint();
    }
    
    private void initializeSpriteNavigation(PokemonSprites sprites) {
        // Create arrays of available sprites and their labels
        java.util.List<String> urlsList = new java.util.ArrayList<>();
        java.util.List<String> labelsList = new java.util.ArrayList<>();
        
        if (sprites.getFrontDefault() != null) {
            urlsList.add(sprites.getFrontDefault());
            labelsList.add("Frontal Normal");
        }
        if (sprites.getBackDefault() != null) {
            urlsList.add(sprites.getBackDefault());
            labelsList.add("Trasero Normal");
        }
        if (sprites.getFrontShiny() != null) {
            urlsList.add(sprites.getFrontShiny());
            labelsList.add("Frontal Shiny");
        }
        if (sprites.getBackShiny() != null) {
            urlsList.add(sprites.getBackShiny());
            labelsList.add("Trasero Shiny");
        }
        
        // Convert lists to arrays
        spriteUrls = urlsList.toArray(new String[0]);
        spriteLabels = labelsList.toArray(new String[0]);
        currentSpriteIndex = 0;
    }
    
    private void showCurrentSprite() {
        if (spriteUrls == null || spriteUrls.length == 0) {
            spriteLabel.setIcon(null);
            spriteLabel.setText("No hay sprites disponibles");
            return;
        }
        
        try {
            // Load and display the current sprite
            String currentUrl = spriteUrls[currentSpriteIndex];
            String currentLabel = spriteLabels[currentSpriteIndex];
            
            URL url = new URL(currentUrl);
            java.awt.image.BufferedImage bufferedImage = ImageIO.read(url);
            
            if (bufferedImage != null) {
                int targetSize = 150;
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
        if (spriteUrls != null && spriteUrls.length > 0) {
            currentSpriteIndex = (currentSpriteIndex + 1) % spriteUrls.length;
            showCurrentSprite();
        }
    }
    
    private void showPreviousSprite() {
        if (spriteUrls != null && spriteUrls.length > 0) {
            currentSpriteIndex = (currentSpriteIndex - 1 + spriteUrls.length) % spriteUrls.length;
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