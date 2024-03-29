package com.timecat.data.system.model.habit;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.timecat.data.system.model.StringUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import javax.inject.Inject;

/**
 * The thing that the user wants to track.
 */
public class Habit {
    public static final int AT_LEAST = 0;

    public static final int AT_MOST = 1;

    public static final String HABIT_URI_FORMAT = "content://com.time.cat/habit/%d";

    public static final int NUMBER_HABIT = 1;

    public static final int YES_NO_HABIT = 0;

    @Nullable
    public Long id;

    @NonNull
    private HabitData data;

    @NonNull
    private StreakList streaks;

    @NonNull
    private ScoreList scores;

    @NonNull
    private RepetitionList repetitions;

    @NonNull
    private CheckmarkList checkmarks;

    private ModelObservable observable = new ModelObservable();

    /**
     * Constructs a habit with default data.
     * <p>
     * The habit is not archived, not highlighted, has no reminders and is
     * placed in the last position of the list of habits.
     */
    @Inject
    Habit(@NonNull ModelFactory factory) {
        this.data = new HabitData();
        checkmarks = factory.buildCheckmarkList(this);
        streaks = factory.buildStreakList(this);
        scores = factory.buildScoreList(this);
        repetitions = factory.buildRepetitionList(this);
    }

    Habit(@NonNull ModelFactory factory, @NonNull HabitData data) {
        this.data = new HabitData(data);
        checkmarks = factory.buildCheckmarkList(this);
        streaks = factory.buildStreakList(this);
        scores = factory.buildScoreList(this);
        repetitions = factory.buildRepetitionList(this);
        observable = new ModelObservable();
    }

    /**
     * Clears the reminder for a habit.
     */
    public synchronized void clearReminder() {
        data.reminder = null;
        observable.notifyListeners();
    }

    /**
     * Copies all the attributes of the specified habit into this habit
     *
     * @param model the model whose attributes should be copied from
     */
    public synchronized void copyFrom(@NonNull Habit model) {
        this.data = new HabitData(model.data);
        observable.notifyListeners();
    }

    /**
     * List of checkmarks belonging to this habit.
     */
    @NonNull
    public synchronized CheckmarkList getCheckmarks() {
        return checkmarks;
    }

    /**
     * Color of the habit.
     * <p>
     * This number is not an android.graphics.Color, but an index to the
     * activity color palette, which changes according to the theme. To convert
     * this color into an android.graphics.Color, use ColorHelper.getColor(context,
     * habit.color).
     */
    @NonNull
    public synchronized Integer getColor() {
        return data.color;
    }

    public synchronized void setColor(@NonNull Integer color) {
        data.color = color;
    }

    @NonNull
    public synchronized String getDescription() {
        return data.description;
    }

    public synchronized void setDescription(@NonNull String description) {
        data.description = description;
    }

    @NonNull
    public synchronized Frequency getFrequency() {
        return data.frequency;
    }

    public synchronized void setFrequency(@NonNull Frequency frequency) {
        data.frequency = frequency;
    }

    @Nullable
    public synchronized Long getId() {
        return id;
    }

    public synchronized void setId(@Nullable Long id) {
        this.id = id;
    }

    @NonNull
    public synchronized String getName() {
        return data.name;
    }

    public synchronized void setName(@NonNull String name) {
        data.name = name;
    }

    public ModelObservable getObservable() {
        return observable;
    }

    /**
     * Returns the reminder for this habit.
     * <p>
     * Before calling this method, you should call {@link #hasReminder()} to
     * verify that a reminder does exist, otherwise an exception will be
     * thrown.
     *
     * @return the reminder for this habit
     * @throws IllegalStateException if habit has no reminder
     */
    @NonNull
    public synchronized Reminder getReminder() {
        if (data.reminder == null) throw new IllegalStateException();
        return data.reminder;
    }

    public synchronized void setReminder(@Nullable Reminder reminder) {
        data.reminder = reminder;
    }

    @NonNull
    public RepetitionList getRepetitions() {
        return repetitions;
    }

    @NonNull
    public ScoreList getScores() {
        return scores;
    }

    @NonNull
    public StreakList getStreaks() {
        return streaks;
    }

    public synchronized int getTargetType() {
        return data.targetType;
    }

    public synchronized void setTargetType(int targetType) {
        if (targetType != AT_LEAST && targetType != AT_MOST)
            throw new IllegalArgumentException(
                    String.format("invalid targetType: %d", targetType));
        data.targetType = targetType;
    }

    public synchronized double getTargetValue() {
        return data.targetValue;
    }

    public synchronized void setTargetValue(double targetValue) {
        if (targetValue < 0) throw new IllegalArgumentException();
        data.targetValue = targetValue;
    }

    public synchronized int getType() {
        return data.type;
    }

    public synchronized void setType(int type) {
        if (type != YES_NO_HABIT && type != NUMBER_HABIT)
            throw new IllegalArgumentException();

        data.type = type;
    }

    @NonNull
    public synchronized String getUnit() {
        return data.unit;
    }

    public synchronized void setUnit(@NonNull String unit) {
        data.unit = unit;
    }

    /**
     * Returns the public URI that identifies this habit
     *
     * @return the URI
     */
    public String getUriString() {
        return String.format(Locale.US, HABIT_URI_FORMAT, getId());
    }

    public synchronized boolean hasId() {
        return getId() != null;
    }

    /**
     * Returns whether the habit has a reminder.
     *
     * @return true if habit has reminder, false otherwise
     */
    public synchronized boolean hasReminder() {
        return data.reminder != null;
    }

    public void invalidateNewerThan(Timestamp timestamp) {
        getScores().invalidateNewerThan(timestamp);
        getCheckmarks().invalidateNewerThan(timestamp);
        getStreaks().invalidateNewerThan(timestamp);
    }

    public synchronized boolean isArchived() {
        return data.archived;
    }

    public synchronized void setArchived(boolean archived) {
        data.archived = archived;
    }

    public synchronized boolean isCompletedToday() {
        int todayCheckmark = getCheckmarks().getTodayValue();
        if (isNumerical()) {
            if (getTargetType() == AT_LEAST)
                return todayCheckmark >= data.targetValue;
            else
                return todayCheckmark <= data.targetValue;
        } else return (todayCheckmark != Checkmark.UNCHECKED);
    }

    public synchronized boolean isNumerical() {
        return data.type == NUMBER_HABIT;
    }

    public HabitData getData() {
        return new HabitData(data);
    }

    public Integer getPosition() {
        return data.position;
    }

    public void setPosition(int newPosition) {
        data.position = newPosition;
    }

    public static final class HabitData {
        @NonNull
        public String name;

        @NonNull
        public String description;

        @NonNull
        public Frequency frequency;

        public int color;

        public boolean archived;

        public int targetType;

        public double targetValue;

        public int type;

        @NonNull
        public String unit;

        @Nullable
        public Reminder reminder;

        public int position;

        public HabitData() {
            this.color = Color.parseColor("#a9b6f9");
            this.archived = false;
            this.frequency = new Frequency(3, 7);
            this.type = YES_NO_HABIT;
            this.name = "";
            this.description = "";
            this.targetType = AT_LEAST;
            this.targetValue = 100;
            this.unit = "";
            this.position = 0;
        }

        public HabitData(@NonNull HabitData model) {
            this.name = model.name;
            this.description = model.description;
            this.frequency = model.frequency;
            this.color = model.color;
            this.archived = model.archived;
            this.targetType = model.targetType;
            this.targetValue = model.targetValue;
            this.type = model.type;
            this.unit = model.unit;
            this.reminder = model.reminder;
            this.position = model.position;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, StringUtils.defaultToStringStyle())
                    .append("name", name)
                    .append("description", description)
                    .append("frequency", frequency)
                    .append("color", color)
                    .append("archived", archived)
                    .append("targetType", targetType)
                    .append("targetValue", targetValue)
                    .append("type", type)
                    .append("unit", unit)
                    .append("reminder", reminder)
                    .append("position", position)
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            HabitData habitData = (HabitData) o;

            return new EqualsBuilder()
                    .append(color, habitData.color)
                    .append(archived, habitData.archived)
                    .append(targetType, habitData.targetType)
                    .append(targetValue, habitData.targetValue)
                    .append(type, habitData.type)
                    .append(name, habitData.name)
                    .append(description, habitData.description)
                    .append(frequency, habitData.frequency)
                    .append(unit, habitData.unit)
                    .append(reminder, habitData.reminder)
                    .append(position, habitData.position)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(name)
                    .append(description)
                    .append(frequency)
                    .append(color)
                    .append(archived)
                    .append(targetType)
                    .append(targetValue)
                    .append(type)
                    .append(unit)
                    .append(reminder)
                    .append(position)
                    .toHashCode();
        }
    }

    @NotNull
    @Override
    public String toString() {
        return new ToStringBuilder(this, StringUtils.defaultToStringStyle())
                .append("id", id)
                .append("data", data)
                .toString();
    }
}
