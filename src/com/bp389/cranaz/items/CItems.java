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
		        + "Les zombies sont intelligents mais aveugles; " + "A titre d'indication:\n" + "- Sneak -> Reper� � 6 blocs.\n"
		        + "- Marche -> Vu � 15 blocs.\n" + "- Sprint -> Vu � 30 blocs.", "Armes et utilisation\n\n" + "Utiliser de bonnes armes est important;"
		        + "- Les armes � feu sont puissantes et permettent " + "de tirer de loin. Cependant, elles sont bruyantes.",
		        "- Les armes de mel�e sont puissantes et silencieuses. " + "Il faut cependant d'�tre proche.\n"
		                + "- L'arc est une excellente arme. Elle est puissante, " + "tire de loin, et est silencieuse.", "Trouver des ressources\n\n"
		                + "Trouver des ressources est essentiel. La plupart " + "des ressources peuvent se trouver dans des coffres. "
		                + "Prenez aussi garde aux objets qui tra�nent, interagir " + "avec le bloc sur lequel ils reposent est souvent tr�s " + "int�ressant.",
		        "Certains objets sont plus rares que " + "d'autres, comme les armes.", "G�rer vos besoins\n\n"
		                + "Cela semble logique, mais il est important de le pr�ciser;" + "buvez et mangez r�guli�rement pour vous maintenir en vie. "
		                + "Une bouteille d'eau vous est fournie � votre arriv�e, " + "servez-vous en et remplissez-la � des points d'eau.",
		        "Heures de sortie\n\n" + "Les zombies ne craignant pas le soleil, sortir de jour "
		                + "n'est pas enti�rement s�r. Mais la nuit, ils sont nombreux " + "et plus forts. Choisissez donc bien l'heure pour vos "
		                + "excursions en territoire zombie.", "Substances utiles\n\n" + "Vous pouvez utiliser des substances chimiques "
		                + "vous permettant d'augmenter drastiquement vos " + "chances de survie. Les amph�tamines " + "en sont un exemple fort.",
		        "Vous pouvez les trouver dans des h�pitaux ou " + "les fabriquer avec de l'eau, du sucre, de la redstone, "
		                + "du pavot(herbe) et des champignons hallucinog�nes " + "(rouge � pois blanc).", "Epilogue\n\n"
		                + "La survie en milieu hostile est difficile;" + "N'oubliez pas les conseils fondamentaux.\n\n" + "                     Steve"));
		try {
			new File("plugins/CranaZ/Divers/survie.yml").createNewFile();
			fc.save(new File("plugins/CranaZ/Divers/survie.yml"));
		} catch(final IOException e) {}
	}

	public void registerRules() {
		final FileConfiguration fc = Loader.plugin.getConfig();
		fc.set("regles", Arrays.asList("Savoir-vivre:\n\n" + "- Etre respectueux envers les autres.\n" + "- Respecter les d�cisions du staff.\n"
		        + "- Parler correctement.\n", "PVP:\n\n" + "Le PVP est autoris� et fait partie " + "int�grante du gameplay. Le staff se "
		        + "r�serve le droit de punir en cas de " + "PVP abusif(SpawnKill...).", "Publicit� et marketing:\n\n" + "Vous �tes autoris� � faire de "
		        + "la pub pour un autre serveur " + "UNIQUEMENT en cas d'accord du staff. " + "Le cas �ch�ant, une sanction relative "
		        + "� la faute sera prononc�e. ", "Spam et flood:\n\n" + "Il est STRICTEMENT interdit de spammer ou "
		        + "de flooder le chat global ou un joueur " + "en particulier. Tout spam sera puni.", "Plaintes:\n\n"
		        + "En cas de probl�me grave avec un autre joueur, " + "vous �tes autoris� � demander une sanction � "
		        + "un membre du staff autoris� � en appliquer. " + "Votre demande doit clairement expliquer les faits.", "Principe des r�gles:\n\n"
		        + "Ces r�gles sont consid�r�es comme lues. " + "Tout manquement � une de ces r�gles " + "entra�nera une sanction relative � la faute.\n\n"
		        + "Le staff de CranaZ vous remercie de " + "votre compr�hension."));
		try {
			new File("plugins/CranaZ/Divers/regles.yml").createNewFile();
			fc.save(new File("plugins/CranaZ/Divers/regles.yml"));
		} catch(final IOException e) {}
	}
}
