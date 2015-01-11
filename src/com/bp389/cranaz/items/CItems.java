package com.bp389.cranaz.items;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;

import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.Loader;

public final class CItems extends Loadable {

	@Override
	public void onEnable() {
		Items.recipes();
		new File("plugins/CranaZ/Divers/").mkdirs();
		this.registerRules();
		this.registerSurvive1();
	}

	public void registerSurvive1() {
		final FileConfiguration fc = Loader.plugin.getConfig();
		fc.set("survie", Arrays.asList("\n\n\n\nGuide de survie en milieu hostile I\n\n\n\n", "Comportement des zombies\n\n"
		        + "Les zombies sont intelligents mais aveugles; " + "A titre d'indication:\n" + "- Sneak -> Reperé à 6 blocs.\n"
		        + "- Marche -> Vu à 15 blocs.\n" + "- Sprint -> Vu à 30 blocs.", "Armes et utilisation\n\n" + "Utiliser de bonnes armes est important;"
		        + "- Les armes à feu sont puissantes et permettent " + "de tirer de loin. Cependant, elles sont bruyantes.",
		        "- Les armes de melée sont puissantes et silencieuses. " + "Il faut cependant d'être proche.\n"
		                + "- L'arc est une excellente arme. Elle est puissante, " + "tire de loin, et est silencieuse.", "Trouver des ressources\n\n"
		                + "Trouver des ressources est essentiel. La plupart " + "des ressources peuvent se trouver dans des coffres. "
		                + "Prenez aussi garde aux objets qui traînent, interagir " + "avec le bloc sur lequel ils reposent est souvent très " + "intéressant.",
		        "Certains objets sont plus rares que " + "d'autres, comme les armes.", "Gérer vos besoins\n\n"
		                + "Cela semble logique, mais il est important de le préciser;" + "buvez et mangez régulièrement pour vous maintenir en vie. "
		                + "Une bouteille d'eau vous est fournie à votre arrivée, " + "servez-vous en et remplissez-la à des points d'eau.",
		        "Heures de sortie\n\n" + "Les zombies ne craignant pas le soleil, sortir de jour "
		                + "n'est pas entièrement sûr. Mais la nuit, ils sont nombreux " + "et plus forts. Choisissez donc bien l'heure pour vos "
		                + "excursions en territoire zombie.", "Substances utiles\n\n" + "Vous pouvez utiliser des substances chimiques "
		                + "vous permettant d'augmenter drastiquement vos " + "chances de survie. Les amphétamines " + "en sont un exemple fort.",
		        "Vous pouvez les trouver dans des hôpitaux ou " + "les fabriquer avec de l'eau, du sucre, de la redstone, "
		                + "du pavot(herbe) et des champignons hallucinogènes " + "(rouge à pois blanc).", "Epilogue\n\n"
		                + "La survie en milieu hostile est difficile;" + "N'oubliez pas les conseils fondamentaux.\n\n" + "                     Steve"));
		try {
			new File("plugins/CranaZ/Divers/survie.yml").createNewFile();
			fc.save(new File("plugins/CranaZ/Divers/survie.yml"));
		} catch(final IOException e) {}
	}

	public void registerRules() {
		final FileConfiguration fc = Loader.plugin.getConfig();
		fc.set("regles", Arrays.asList("Savoir-vivre:\n\n" + "- Etre respectueux envers les autres.\n" + "- Respecter les décisions du staff.\n"
		        + "- Parler correctement.\n", "PVP:\n\n" + "Le PVP est autorisé et fait partie " + "intégrante du gameplay. Le staff se "
		        + "réserve le droit de punir en cas de " + "PVP abusif(SpawnKill...).", "Publicité et marketing:\n\n" + "Vous êtes autorisé à faire de "
		        + "la pub pour un autre serveur " + "UNIQUEMENT en cas d'accord du staff. " + "Le cas échéant, une sanction relative "
		        + "à la faute sera prononcée. ", "Spam et flood:\n\n" + "Il est STRICTEMENT interdit de spammer ou "
		        + "de flooder le chat global ou un joueur " + "en particulier. Tout spam sera puni.", "Plaintes:\n\n"
		        + "En cas de problème grave avec un autre joueur, " + "vous êtes autorisé à demander une sanction à "
		        + "un membre du staff autorisé à en appliquer. " + "Votre demande doit clairement expliquer les faits.", "Principe des règles:\n\n"
		        + "Ces règles sont considérées comme lues. " + "Tout manquement à une de ces règles " + "entraînera une sanction relative à la faute.\n\n"
		        + "Le staff de CranaZ vous remercie de " + "votre compréhension."));
		try {
			new File("plugins/CranaZ/Divers/regles.yml").createNewFile();
			fc.save(new File("plugins/CranaZ/Divers/regles.yml"));
		} catch(final IOException e) {}
	}
}
