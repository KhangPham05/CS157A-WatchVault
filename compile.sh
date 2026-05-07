#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────
#  WatchVault – compile script  (macOS / Linux)
#  Usage: chmod +x compile.sh && ./compile.sh
# ─────────────────────────────────────────────────────────────
set -e

JAR="lib/mysql-connector-j-9.1.0.jar"
OUT="out"
SRC_DIR="src/main/java"

echo "==> Checking for MySQL Connector/J..."
if [ ! -f "$JAR" ]; then
  echo "    Downloading MySQL Connector/J 9.1.0..."
  mkdir -p lib
  curl -L -o "$JAR" \
    "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.1.0/mysql-connector-j-9.1.0.jar"
  echo "    Downloaded."
else
  echo "    Found: $JAR"
fi

echo "==> Compiling Java sources..."
mkdir -p "$OUT"
find "$SRC_DIR" -name "*.java" | xargs javac -cp "$JAR" -d "$OUT" --release 14

echo "==> Packaging WatchVault.jar..."
cat > "$OUT/META-INF/MANIFEST.MF" << 'EOF'
Manifest-Version: 1.0
Main-Class: watchvault.ui.WatchVaultCLI
Class-Path: lib/mysql-connector-j-9.1.0.jar
EOF

cd "$OUT"
jar cfm ../WatchVault.jar META-INF/MANIFEST.MF $(find . -name "*.class" | sed 's|^\./||')
cd ..

echo ""
echo "  ✓ Build successful! WatchVault.jar created."
echo ""
echo "  Before running:"
echo "    1. Start MySQL and run:  mysql -u root -p < sql/01_schema.sql"
echo "    2.                       mysql -u root -p < sql/02_seed_data.sql"
echo "    3. Edit DBConnection.java if your MySQL password is not empty."
echo ""
echo "  To run:"
echo "    java -jar WatchVault.jar"
echo ""
echo "  Default admin login:"
echo "    Email:    admin@watchvault.com"
echo "    Password: admin123"
