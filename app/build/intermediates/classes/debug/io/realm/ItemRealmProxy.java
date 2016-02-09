package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.chienpao.notepad.notepad.model.Item;
import io.realm.RealmFieldType;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.LinkView;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemRealmProxy extends Item
    implements RealmObjectProxy {

    static final class ItemColumnInfo extends ColumnInfo {

        public final long nameIndex;
        public final long countIndex;
        public final long noteIndex;
        public final long idIndex;

        ItemColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(4);
            this.nameIndex = getValidColumnIndex(path, table, "Item", "name");
            indicesMap.put("name", this.nameIndex);

            this.countIndex = getValidColumnIndex(path, table, "Item", "count");
            indicesMap.put("count", this.countIndex);

            this.noteIndex = getValidColumnIndex(path, table, "Item", "note");
            indicesMap.put("note", this.noteIndex);

            this.idIndex = getValidColumnIndex(path, table, "Item", "id");
            indicesMap.put("id", this.idIndex);

            setIndicesMap(indicesMap);
        }
    }

    private final ItemColumnInfo columnInfo;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("name");
        fieldNames.add("count");
        fieldNames.add("note");
        fieldNames.add("id");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    ItemRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (ItemColumnInfo) columnInfo;
    }

    @Override
    @SuppressWarnings("cast")
    public String getName() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(columnInfo.nameIndex);
    }

    @Override
    public void setName(String value) {
        realm.checkIfValid();
        if (value == null) {
            row.setNull(columnInfo.nameIndex);
            return;
        }
        row.setString(columnInfo.nameIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public int getCount() {
        realm.checkIfValid();
        return (int) row.getLong(columnInfo.countIndex);
    }

    @Override
    public void setCount(int value) {
        realm.checkIfValid();
        row.setLong(columnInfo.countIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String getNote() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(columnInfo.noteIndex);
    }

    @Override
    public void setNote(String value) {
        realm.checkIfValid();
        if (value == null) {
            row.setNull(columnInfo.noteIndex);
            return;
        }
        row.setString(columnInfo.noteIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long getId() {
        realm.checkIfValid();
        return (long) row.getLong(columnInfo.idIndex);
    }

    @Override
    public void setId(long value) {
        realm.checkIfValid();
        row.setLong(columnInfo.idIndex, value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_Item")) {
            Table table = transaction.getTable("class_Item");
            table.addColumn(RealmFieldType.STRING, "name", Table.NULLABLE);
            table.addColumn(RealmFieldType.INTEGER, "count", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.STRING, "note", Table.NULLABLE);
            table.addColumn(RealmFieldType.INTEGER, "id", Table.NOT_NULLABLE);
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_Item");
    }

    public static ItemColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_Item")) {
            Table table = transaction.getTable("class_Item");
            if (table.getColumnCount() != 4) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 4 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 4; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final ItemColumnInfo columnInfo = new ItemColumnInfo(transaction.getPath(), table);

            if (!columnTypes.containsKey("name")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'name' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("name") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'name' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.nameIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'name' is required. Either set @Required to field 'name' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            if (!columnTypes.containsKey("count")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'count' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("count") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'int' for field 'count' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.countIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'count' does support null values in the existing Realm file. Use corresponding boxed type for field 'count' or migrate using io.realm.internal.Table.convertColumnToNotNullable().");
            }
            if (!columnTypes.containsKey("note")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'note' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("note") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'note' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.noteIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'note' is required. Either set @Required to field 'note' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            if (!columnTypes.containsKey("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'id' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("id") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'long' for field 'id' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.idIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'id' does support null values in the existing Realm file. Use corresponding boxed type for field 'id' or migrate using io.realm.internal.Table.convertColumnToNotNullable().");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The Item class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_Item";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static Item createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        Item obj = realm.createObject(Item.class);
        if (json.has("name")) {
            if (json.isNull("name")) {
                obj.setName(null);
            } else {
                obj.setName((String) json.getString("name"));
            }
        }
        if (json.has("count")) {
            if (json.isNull("count")) {
                throw new IllegalArgumentException("Trying to set non-nullable field count to null.");
            } else {
                obj.setCount((int) json.getInt("count"));
            }
        }
        if (json.has("note")) {
            if (json.isNull("note")) {
                obj.setNote(null);
            } else {
                obj.setNote((String) json.getString("note"));
            }
        }
        if (json.has("id")) {
            if (json.isNull("id")) {
                throw new IllegalArgumentException("Trying to set non-nullable field id to null.");
            } else {
                obj.setId((long) json.getLong("id"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    public static Item createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        Item obj = realm.createObject(Item.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    obj.setName(null);
                } else {
                    obj.setName((String) reader.nextString());
                }
            } else if (name.equals("count")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field count to null.");
                } else {
                    obj.setCount((int) reader.nextInt());
                }
            } else if (name.equals("note")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    obj.setNote(null);
                } else {
                    obj.setNote((String) reader.nextString());
                }
            } else if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field id to null.");
                } else {
                    obj.setId((long) reader.nextLong());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static Item copyOrUpdate(Realm realm, Item object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static Item copy(Realm realm, Item newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        Item realmObject = realm.createObject(Item.class);
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setName(newObject.getName());
        realmObject.setCount(newObject.getCount());
        realmObject.setNote(newObject.getNote());
        realmObject.setId(newObject.getId());
        return realmObject;
    }

    public static Item createDetachedCopy(Item realmObject, int currentDepth, int maxDepth, Map<RealmObject, CacheData<RealmObject>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<Item> cachedObject = (CacheData) cache.get(realmObject);
        Item standaloneObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return cachedObject.object;
            } else {
                standaloneObject = cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            standaloneObject = new Item();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmObject>(currentDepth, standaloneObject));
        }
        standaloneObject.setName(realmObject.getName());
        standaloneObject.setCount(realmObject.getCount());
        standaloneObject.setNote(realmObject.getNote());
        standaloneObject.setId(realmObject.getId());
        return standaloneObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Item = [");
        stringBuilder.append("{name:");
        stringBuilder.append(getName() != null ? getName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{count:");
        stringBuilder.append(getCount());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{note:");
        stringBuilder.append(getNote() != null ? getNote() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{id:");
        stringBuilder.append(getId());
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        String realmName = realm.getPath();
        String tableName = row.getTable().getName();
        long rowIndex = row.getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRealmProxy aItem = (ItemRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aItem.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aItem.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aItem.row.getIndex()) return false;

        return true;
    }

}
