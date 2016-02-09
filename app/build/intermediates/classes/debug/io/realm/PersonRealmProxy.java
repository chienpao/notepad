package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.chienpao.notepad.notepad.model.Cat;
import com.chienpao.notepad.notepad.model.Dog;
import com.chienpao.notepad.notepad.model.Person;
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

public class PersonRealmProxy extends Person
    implements RealmObjectProxy {

    static final class PersonColumnInfo extends ColumnInfo {

        public final long nameIndex;
        public final long ageIndex;
        public final long dogIndex;
        public final long catsIndex;
        public final long idIndex;

        PersonColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(5);
            this.nameIndex = getValidColumnIndex(path, table, "Person", "name");
            indicesMap.put("name", this.nameIndex);

            this.ageIndex = getValidColumnIndex(path, table, "Person", "age");
            indicesMap.put("age", this.ageIndex);

            this.dogIndex = getValidColumnIndex(path, table, "Person", "dog");
            indicesMap.put("dog", this.dogIndex);

            this.catsIndex = getValidColumnIndex(path, table, "Person", "cats");
            indicesMap.put("cats", this.catsIndex);

            this.idIndex = getValidColumnIndex(path, table, "Person", "id");
            indicesMap.put("id", this.idIndex);

            setIndicesMap(indicesMap);
        }
    }

    private final PersonColumnInfo columnInfo;
    private RealmList<Cat> catsRealmList;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("name");
        fieldNames.add("age");
        fieldNames.add("dog");
        fieldNames.add("cats");
        fieldNames.add("id");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    PersonRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (PersonColumnInfo) columnInfo;
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
    public int getAge() {
        realm.checkIfValid();
        return (int) row.getLong(columnInfo.ageIndex);
    }

    @Override
    public void setAge(int value) {
        realm.checkIfValid();
        row.setLong(columnInfo.ageIndex, value);
    }

    @Override
    public Dog getDog() {
        realm.checkIfValid();
        if (row.isNullLink(columnInfo.dogIndex)) {
            return null;
        }
        return realm.get(com.chienpao.notepad.notepad.model.Dog.class, row.getLink(columnInfo.dogIndex));
    }

    @Override
    public void setDog(Dog value) {
        realm.checkIfValid();
        if (value == null) {
            row.nullifyLink(columnInfo.dogIndex);
            return;
        }
        if (!value.isValid()) {
            throw new IllegalArgumentException("'value' is not a valid managed object.");
        }
        if (value.realm != this.realm) {
            throw new IllegalArgumentException("'value' belongs to a different Realm.");
        }
        row.setLink(columnInfo.dogIndex, value.row.getIndex());
    }

    @Override
    public RealmList<Cat> getCats() {
        realm.checkIfValid();
        // use the cached value if available
        if (catsRealmList != null) {
            return catsRealmList;
        } else {
            LinkView linkView = row.getLinkList(columnInfo.catsIndex);
            catsRealmList = new RealmList<Cat>(Cat.class, linkView, realm);
            return catsRealmList;
        }
    }

    @Override
    public void setCats(RealmList<Cat> value) {
        realm.checkIfValid();
        LinkView links = row.getLinkList(columnInfo.catsIndex);
        links.clear();
        if (value == null) {
            return;
        }
        for (RealmObject linkedObject : (RealmList<? extends RealmObject>) value) {
            if (!linkedObject.isValid()) {
                throw new IllegalArgumentException("Each element of 'value' must be a valid managed object.");
            }
            if (linkedObject.realm != this.realm) {
                throw new IllegalArgumentException("Each element of 'value' must belong to the same Realm.");
            }
            links.add(linkedObject.row.getIndex());
        }
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
        if (!transaction.hasTable("class_Person")) {
            Table table = transaction.getTable("class_Person");
            table.addColumn(RealmFieldType.STRING, "name", Table.NULLABLE);
            table.addColumn(RealmFieldType.INTEGER, "age", Table.NOT_NULLABLE);
            if (!transaction.hasTable("class_Dog")) {
                DogRealmProxy.initTable(transaction);
            }
            table.addColumnLink(RealmFieldType.OBJECT, "dog", transaction.getTable("class_Dog"));
            if (!transaction.hasTable("class_Cat")) {
                CatRealmProxy.initTable(transaction);
            }
            table.addColumnLink(RealmFieldType.LIST, "cats", transaction.getTable("class_Cat"));
            table.addColumn(RealmFieldType.INTEGER, "id", Table.NOT_NULLABLE);
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_Person");
    }

    public static PersonColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_Person")) {
            Table table = transaction.getTable("class_Person");
            if (table.getColumnCount() != 5) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 5 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 5; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final PersonColumnInfo columnInfo = new PersonColumnInfo(transaction.getPath(), table);

            if (!columnTypes.containsKey("name")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'name' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("name") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'name' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.nameIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'name' is required. Either set @Required to field 'name' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            if (!columnTypes.containsKey("age")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'age' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("age") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'int' for field 'age' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.ageIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'age' does support null values in the existing Realm file. Use corresponding boxed type for field 'age' or migrate using io.realm.internal.Table.convertColumnToNotNullable().");
            }
            if (!columnTypes.containsKey("dog")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'dog' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("dog") != RealmFieldType.OBJECT) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'Dog' for field 'dog'");
            }
            if (!transaction.hasTable("class_Dog")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing class 'class_Dog' for field 'dog'");
            }
            Table table_2 = transaction.getTable("class_Dog");
            if (!table.getLinkTarget(columnInfo.dogIndex).hasSameSchema(table_2)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid RealmObject for field 'dog': '" + table.getLinkTarget(columnInfo.dogIndex).getName() + "' expected - was '" + table_2.getName() + "'");
            }
            if (!columnTypes.containsKey("cats")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'cats'");
            }
            if (columnTypes.get("cats") != RealmFieldType.LIST) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'Cat' for field 'cats'");
            }
            if (!transaction.hasTable("class_Cat")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing class 'class_Cat' for field 'cats'");
            }
            Table table_3 = transaction.getTable("class_Cat");
            if (!table.getLinkTarget(columnInfo.catsIndex).hasSameSchema(table_3)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid RealmList type for field 'cats': '" + table.getLinkTarget(columnInfo.catsIndex).getName() + "' expected - was '" + table_3.getName() + "'");
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
            throw new RealmMigrationNeededException(transaction.getPath(), "The Person class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_Person";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static Person createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        Person obj = realm.createObject(Person.class);
        if (json.has("name")) {
            if (json.isNull("name")) {
                obj.setName(null);
            } else {
                obj.setName((String) json.getString("name"));
            }
        }
        if (json.has("age")) {
            if (json.isNull("age")) {
                throw new IllegalArgumentException("Trying to set non-nullable field age to null.");
            } else {
                obj.setAge((int) json.getInt("age"));
            }
        }
        if (json.has("dog")) {
            if (json.isNull("dog")) {
                obj.setDog(null);
            } else {
                com.chienpao.notepad.notepad.model.Dog dogObj = DogRealmProxy.createOrUpdateUsingJsonObject(realm, json.getJSONObject("dog"), update);
                obj.setDog(dogObj);
            }
        }
        if (json.has("cats")) {
            if (json.isNull("cats")) {
                obj.setCats(null);
            } else {
                obj.getCats().clear();
                JSONArray array = json.getJSONArray("cats");
                for (int i = 0; i < array.length(); i++) {
                    com.chienpao.notepad.notepad.model.Cat item = CatRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    obj.getCats().add(item);
                }
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
    public static Person createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        Person obj = realm.createObject(Person.class);
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
            } else if (name.equals("age")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field age to null.");
                } else {
                    obj.setAge((int) reader.nextInt());
                }
            } else if (name.equals("dog")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    obj.setDog(null);
                } else {
                    com.chienpao.notepad.notepad.model.Dog dogObj = DogRealmProxy.createUsingJsonStream(realm, reader);
                    obj.setDog(dogObj);
                }
            } else if (name.equals("cats")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    obj.setCats(null);
                } else {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.chienpao.notepad.notepad.model.Cat item = CatRealmProxy.createUsingJsonStream(realm, reader);
                        obj.getCats().add(item);
                    }
                    reader.endArray();
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

    public static Person copyOrUpdate(Realm realm, Person object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static Person copy(Realm realm, Person newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        Person realmObject = realm.createObject(Person.class);
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setName(newObject.getName());
        realmObject.setAge(newObject.getAge());

        com.chienpao.notepad.notepad.model.Dog dogObj = newObject.getDog();
        if (dogObj != null) {
            com.chienpao.notepad.notepad.model.Dog cachedog = (com.chienpao.notepad.notepad.model.Dog) cache.get(dogObj);
            if (cachedog != null) {
                realmObject.setDog(cachedog);
            } else {
                realmObject.setDog(DogRealmProxy.copyOrUpdate(realm, dogObj, update, cache));
            }
        } else {
            realmObject.setDog(null);
        }

        RealmList<Cat> catsList = newObject.getCats();
        if (catsList != null) {
            RealmList<Cat> catsRealmList = realmObject.getCats();
            for (int i = 0; i < catsList.size(); i++) {
                Cat catsItem = catsList.get(i);
                Cat cachecats = (Cat) cache.get(catsItem);
                if (cachecats != null) {
                    catsRealmList.add(cachecats);
                } else {
                    catsRealmList.add(CatRealmProxy.copyOrUpdate(realm, catsList.get(i), update, cache));
                }
            }
        }

        realmObject.setId(newObject.getId());
        return realmObject;
    }

    public static Person createDetachedCopy(Person realmObject, int currentDepth, int maxDepth, Map<RealmObject, CacheData<RealmObject>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<Person> cachedObject = (CacheData) cache.get(realmObject);
        Person standaloneObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return cachedObject.object;
            } else {
                standaloneObject = cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            standaloneObject = new Person();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmObject>(currentDepth, standaloneObject));
        }
        standaloneObject.setName(realmObject.getName());
        standaloneObject.setAge(realmObject.getAge());

        // Deep copy of dog
        standaloneObject.setDog(DogRealmProxy.createDetachedCopy(realmObject.getDog(), currentDepth + 1, maxDepth, cache));

        // Deep copy of cats
        if (currentDepth == maxDepth) {
            standaloneObject.setCats(null);
        } else {
            RealmList<Cat> managedcatsList = realmObject.getCats();
            RealmList<Cat> standalonecatsList = new RealmList<Cat>();
            standaloneObject.setCats(standalonecatsList);
            int nextDepth = currentDepth + 1;
            int size = managedcatsList.size();
            for (int i = 0; i < size; i++) {
                Cat item = CatRealmProxy.createDetachedCopy(managedcatsList.get(i), nextDepth, maxDepth, cache);
                standalonecatsList.add(item);
            }
        }
        standaloneObject.setId(realmObject.getId());
        return standaloneObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Person = [");
        stringBuilder.append("{name:");
        stringBuilder.append(getName() != null ? getName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{age:");
        stringBuilder.append(getAge());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{dog:");
        stringBuilder.append(getDog() != null ? "Dog" : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{cats:");
        stringBuilder.append("RealmList<Cat>[").append(getCats().size()).append("]");
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
        PersonRealmProxy aPerson = (PersonRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aPerson.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aPerson.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aPerson.row.getIndex()) return false;

        return true;
    }

}
