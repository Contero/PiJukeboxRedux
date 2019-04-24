module jukeboxrdx {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires transitive javafx.graphics;
	requires java.sql;
	requires jaudiotagger;
	requires javafx.media;
	requires java.desktop;
	requires jflac;
	requires sqlite.jdbc;
	exports org.conterosoft.jukebox;
}