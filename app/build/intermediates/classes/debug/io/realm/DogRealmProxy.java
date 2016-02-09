package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.chienpao.notepad.notepad.model.Dog;
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

public class DogRealmProxy extends Dog
    implements RealmObjectProxy {

    static final class DogColumnInfo extends ColumnInfo {

        public final long nameIndex;

        DogColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(1);
            this.nameIndex = getValidColumnIndex(path, table, "Dog", "name");
            indicesMap.put("name", this.nameIndex);

            setIndicesMap(indicesMap);
        }
    }

    private final DogColumnInfo columnInfo;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("name");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    DogRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (DogColumnInfo) columnInfo;
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

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_Dog")) {
            Table table = transaction.getTable("class_Dog");
            table.addColumn(RealmFieldType.STRING, "name", Table.NULLABLE);
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_Dog");
    }

    public static DogColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_Dog")) {
            Table table = transaction.getTable("class_Dog");
            if (table.getColumnCount() != 1) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 1 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 1; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final DogColumnInfo columnInfo = new DogColumnInfo(transaction.getPath(), table);

            if (!columnTypes.containsKey("name")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'name' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("name") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'name' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.nameIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'name' is required. Either set @Required to field 'name' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The Dog class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_Dog";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static Dog createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        Dog obj = realm.createObject(Dog.class);
        if (json.has("name")) {
            if (json.isNull("name")) {
                obj.setName(null);
            } else {
                obj.setName((String) json.getString("name"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    public static Dog createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        Dog obj = realm.createObject(Dog.class);
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
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static Dog copyOrUpdate(Realm realm, Dog object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static Dog copy(Realm realm, Dog newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        Dog realmObject = realm.createObject(Dog.class);
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setName(newObject.getName());
        return realmObject;
    }

    public static Dog createDetachedCopy(Dog realmObject, int currentDepth, int maxDepth, Map<RealmObject, CacheData<RealmObject>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<Dog> cachedObject = (CacheData) cache.get(realmObject);
        Dog standaloneObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return cachedObject.object;
            } else {
                standaloneObject = cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            standaloneObject = new Dog();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmObject>(currentDepth, standaloneObject));
        }
        standaloneObject.setName(realmObject.getName());
        return standaloneObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Dog = [");
        stringBuilder.append("{name:");
        stringBuilder.append(getName() != null ? getName() : "null");
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
        DogRealmProxy aDog = (DogRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aDog.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aDog.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aDog.row.getIndex()) return false;

        return true;
    }

}
