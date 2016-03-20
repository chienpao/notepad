package io.realm;


import android.util.JsonReader;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Table;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import com.chienpao.notepad.notepad.model.Cat;
import com.chienpao.notepad.notepad.model.Dog;
import com.chienpao.notepad.notepad.model.Item;
import com.chienpao.notepad.notepad.model.Patient;
import com.chienpao.notepad.notepad.model.Person;

@io.realm.annotations.RealmModule
class DefaultRealmModuleMediator extends RealmProxyMediator {

    private static final Set<Class<? extends RealmObject>> MODEL_CLASSES;
    static {
        Set<Class<? extends RealmObject>> modelClasses = new HashSet<Class<? extends RealmObject>>();
        modelClasses.add(Patient.class);
        modelClasses.add(Cat.class);
        modelClasses.add(Item.class);
        modelClasses.add(Person.class);
        modelClasses.add(Dog.class);
        MODEL_CLASSES = Collections.unmodifiableSet(modelClasses);
    }

    @Override
    public Table createTable(Class<? extends RealmObject> clazz, ImplicitTransaction transaction) {
        checkClass(clazz);

        if (clazz.equals(Patient.class)) {
            return PatientRealmProxy.initTable(transaction);
        } else if (clazz.equals(Cat.class)) {
            return CatRealmProxy.initTable(transaction);
        } else if (clazz.equals(Item.class)) {
            return ItemRealmProxy.initTable(transaction);
        } else if (clazz.equals(Person.class)) {
            return PersonRealmProxy.initTable(transaction);
        } else if (clazz.equals(Dog.class)) {
            return DogRealmProxy.initTable(transaction);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public ColumnInfo validateTable(Class<? extends RealmObject> clazz, ImplicitTransaction transaction) {
        checkClass(clazz);

        if (clazz.equals(Patient.class)) {
            return PatientRealmProxy.validateTable(transaction);
        } else if (clazz.equals(Cat.class)) {
            return CatRealmProxy.validateTable(transaction);
        } else if (clazz.equals(Item.class)) {
            return ItemRealmProxy.validateTable(transaction);
        } else if (clazz.equals(Person.class)) {
            return PersonRealmProxy.validateTable(transaction);
        } else if (clazz.equals(Dog.class)) {
            return DogRealmProxy.validateTable(transaction);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public List<String> getFieldNames(Class<? extends RealmObject> clazz) {
        checkClass(clazz);

        if (clazz.equals(Patient.class)) {
            return PatientRealmProxy.getFieldNames();
        } else if (clazz.equals(Cat.class)) {
            return CatRealmProxy.getFieldNames();
        } else if (clazz.equals(Item.class)) {
            return ItemRealmProxy.getFieldNames();
        } else if (clazz.equals(Person.class)) {
            return PersonRealmProxy.getFieldNames();
        } else if (clazz.equals(Dog.class)) {
            return DogRealmProxy.getFieldNames();
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public String getTableName(Class<? extends RealmObject> clazz) {
        checkClass(clazz);

        if (clazz.equals(Patient.class)) {
            return PatientRealmProxy.getTableName();
        } else if (clazz.equals(Cat.class)) {
            return CatRealmProxy.getTableName();
        } else if (clazz.equals(Item.class)) {
            return ItemRealmProxy.getTableName();
        } else if (clazz.equals(Person.class)) {
            return PersonRealmProxy.getTableName();
        } else if (clazz.equals(Dog.class)) {
            return DogRealmProxy.getTableName();
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmObject> E newInstance(Class<E> clazz, ColumnInfo columnInfo) {
        checkClass(clazz);

        if (clazz.equals(Patient.class)) {
            return clazz.cast(new PatientRealmProxy(columnInfo));
        } else if (clazz.equals(Cat.class)) {
            return clazz.cast(new CatRealmProxy(columnInfo));
        } else if (clazz.equals(Item.class)) {
            return clazz.cast(new ItemRealmProxy(columnInfo));
        } else if (clazz.equals(Person.class)) {
            return clazz.cast(new PersonRealmProxy(columnInfo));
        } else if (clazz.equals(Dog.class)) {
            return clazz.cast(new DogRealmProxy(columnInfo));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public Set<Class<? extends RealmObject>> getModelClasses() {
        return MODEL_CLASSES;
    }

    @Override
    public <E extends RealmObject> E copyOrUpdate(Realm realm, E obj, boolean update, Map<RealmObject, RealmObjectProxy> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(Patient.class)) {
            return clazz.cast(PatientRealmProxy.copyOrUpdate(realm, (Patient) obj, update, cache));
        } else if (clazz.equals(Cat.class)) {
            return clazz.cast(CatRealmProxy.copyOrUpdate(realm, (Cat) obj, update, cache));
        } else if (clazz.equals(Item.class)) {
            return clazz.cast(ItemRealmProxy.copyOrUpdate(realm, (Item) obj, update, cache));
        } else if (clazz.equals(Person.class)) {
            return clazz.cast(PersonRealmProxy.copyOrUpdate(realm, (Person) obj, update, cache));
        } else if (clazz.equals(Dog.class)) {
            return clazz.cast(DogRealmProxy.copyOrUpdate(realm, (Dog) obj, update, cache));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmObject> E createOrUpdateUsingJsonObject(Class<E> clazz, Realm realm, JSONObject json, boolean update)
        throws JSONException {
        checkClass(clazz);

        if (clazz.equals(Patient.class)) {
            return clazz.cast(PatientRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(Cat.class)) {
            return clazz.cast(CatRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(Item.class)) {
            return clazz.cast(ItemRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(Person.class)) {
            return clazz.cast(PersonRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(Dog.class)) {
            return clazz.cast(DogRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmObject> E createUsingJsonStream(Class<E> clazz, Realm realm, JsonReader reader)
        throws IOException {
        checkClass(clazz);

        if (clazz.equals(Patient.class)) {
            return clazz.cast(PatientRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(Cat.class)) {
            return clazz.cast(CatRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(Item.class)) {
            return clazz.cast(ItemRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(Person.class)) {
            return clazz.cast(PersonRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(Dog.class)) {
            return clazz.cast(DogRealmProxy.createUsingJsonStream(realm, reader));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmObject> E createDetachedCopy(E realmObject, int maxDepth, Map<RealmObject, RealmObjectProxy.CacheData<RealmObject>> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) realmObject.getClass().getSuperclass();

        if (clazz.equals(Patient.class)) {
            return clazz.cast(PatientRealmProxy.createDetachedCopy((Patient) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(Cat.class)) {
            return clazz.cast(CatRealmProxy.createDetachedCopy((Cat) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(Item.class)) {
            return clazz.cast(ItemRealmProxy.createDetachedCopy((Item) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(Person.class)) {
            return clazz.cast(PersonRealmProxy.createDetachedCopy((Person) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(Dog.class)) {
            return clazz.cast(DogRealmProxy.createDetachedCopy((Dog) realmObject, 0, maxDepth, cache));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

}
