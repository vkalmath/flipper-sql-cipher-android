/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.flipper.sqlcipher.impl;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import java.io.File;

public interface SqliteDatabaseConnectionProvider {

  SQLiteDatabase openDatabase(File databaseFile) throws SQLiteException;
}
