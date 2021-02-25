package com.example.objectifsport.activities;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @After
    public void cleanDate() {
        DataManager.getGoals().clear();
        DataManager.getActivities().clear();
        DataManager.getSports().clear();
        DataManager.save();
    }

    @Test
    public void mainActivityAddSportTest() {
        ViewInteraction tabView = onView(
                allOf(withContentDescription("My Sports"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sliding_tabs),
                                        0),
                                0),
                        isDisplayed()));
        tabView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add_sport), withContentDescription("Add sport"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.sport_name),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Tennis"), closeSoftKeyboard());

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.distance_goal), withText("Distance Goal"),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.add_sport), withText("Add sport"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_name),
                                        3),
                                1),
                        isDisplayed()));
        materialButton.perform(click());
    }

    /*
    @Test
    public void mainActivityAddGoalTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add_sport), withContentDescription("Add sport"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.sport_name),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Tennis"), closeSoftKeyboard());

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.distance_goal), withText("Distance Goal"),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.add_sport), withText("Add sport"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_name),
                                        3),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction tabView = onView(
                allOf(withContentDescription("My Goals"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sliding_tabs),
                                        0),
                                2),
                        isDisplayed()));
        tabView.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.add_goal), withContentDescription("Add goal"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                0),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.activity_description),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        appCompatEditText2.perform(scrollTo(), replaceText("Devenir champion"), closeSoftKeyboard());

        ViewInteraction materialCheckBox2 = onView(
                allOf(withId(R.id.time_goal), withText("Time goal"),
                        childAtPosition(
                                allOf(withId(R.id.time_container),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0)));
        materialCheckBox2.perform(scrollTo(), click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.numPad8), withText("8"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.numPad),
                                        2),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.add_goal), withText("Add goal"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1)));
        materialButton4.perform(scrollTo(), click());
    }
    */

    @Test
    public void mainActivityAddActivityTest() {
        ViewInteraction tabView = onView(
                allOf(withContentDescription("My Sports"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sliding_tabs),
                                        0),
                                0),
                        isDisplayed()));
        tabView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add_sport), withContentDescription("Add sport"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.sport_name),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Tennis"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.sport_name), withText("Tennis"),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.distance_goal), withText("Distance Goal"),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.add_sport), withText("Add sport"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_name),
                                        3),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        tabView = onView(
                allOf(withContentDescription("My Activities"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sliding_tabs),
                                        0),
                                1),
                        isDisplayed()));
        tabView.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.add_activity), withContentDescription("Add activity"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                1),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.activity_description),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("Partie avec Nadal"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.add_activity), withText("Add activity"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_name),
                                        2),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());
    }

    @Test
    public void mainActivityRemoveSportTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add_sport), withContentDescription("Add sport"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.sport_name),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Tennis"), closeSoftKeyboard());

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.distance_goal), withText("Distance Goal"),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.add_sport), withText("Add sport"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_name),
                                        3),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.sports_list),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)))
                .atPosition(0);
        linearLayout.perform(longClick());

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button1), withText("remove"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton2.perform(scrollTo(), click());
    }

    @Test
    public void mainActivityRemoveActivityTest() {
        ViewInteraction tabView = onView(
                allOf(withContentDescription("My Sports"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sliding_tabs),
                                        0),
                                0),
                        isDisplayed()));
        tabView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add_sport), withContentDescription("Add sport"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.sport_name),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Tennis"), closeSoftKeyboard());

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.distance_goal), withText("Distance Goal"),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.add_sport), withText("Add sport"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_name),
                                        3),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        tabView = onView(
                allOf(withContentDescription("My Activities"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sliding_tabs),
                                        0),
                                1),
                        isDisplayed()));
        tabView.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.add_activity), withContentDescription("Add activity"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                1),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.activity_description),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("partie avec Nadal"), closeSoftKeyboard());
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.add_activity), withText("Add activity"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_name),
                                        2),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.activities_list),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)))
                .atPosition(0);
        linearLayout.perform(longClick());

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("remove"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());
    }

    /*
    @Test
    public void mainActivityRemoveNonEmptySportTest() {
        ViewInteraction tabView = onView(
                allOf(withContentDescription("My Sports"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sliding_tabs),
                                        0),
                                0),
                        isDisplayed()));
        tabView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add_sport), withContentDescription("Add sport"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.sport_name),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Tennis"), closeSoftKeyboard());

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.distance_goal), withText("Distance Goal"),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.add_sport), withText("Add sport"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_name),
                                        3),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        tabView = onView(
                allOf(withContentDescription("My Activities"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sliding_tabs),
                                        0),
                                1),
                        isDisplayed()));
        tabView.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.add_activity), withContentDescription("Add activity"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                1),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.activity_description),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("Échauffement"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.add_activity), withText("Add activity"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_name),
                                        2),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction tabView2 = onView(
                allOf(withContentDescription("My Goals"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sliding_tabs),
                                        0),
                                2),
                        isDisplayed()));
        tabView2.perform(click());

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.add_goal), withContentDescription("Add goal"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                1),
                        isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.activity_description),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        appCompatEditText3.perform(scrollTo(), replaceText("Devenir champion"), closeSoftKeyboard());

        ViewInteraction materialCheckBox2 = onView(
                allOf(withId(R.id.time_goal), withText("Time goal"),
                        childAtPosition(
                                allOf(withId(R.id.time_container),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0)));
        materialCheckBox2.perform(scrollTo(), click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.numPad2), withText("2"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.numPad),
                                        0),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        materialButton4.perform(scrollTo(), click());

        ViewInteraction materialCheckBox3 = onView(
                allOf(withId(R.id.deadline), withText("Deadline"),
                        childAtPosition(
                                allOf(withId(R.id.deadline_container),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                2)),
                                0)));
        materialCheckBox3.perform(scrollTo(), click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton5.perform(scrollTo(), click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.add_goal), withText("Add goal"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1)));
        materialButton6.perform(scrollTo(), click());

        ViewInteraction tabView3 = onView(
                allOf(withContentDescription("My Sports"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sliding_tabs),
                                        0),
                                0),
                        isDisplayed()));
        tabView3.perform(click());

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.sports_list),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)))
                .atPosition(0);
        linearLayout.perform(longClick());

        ViewInteraction materialButton7 = onView(
                allOf(withId(android.R.id.button1), withText("remove"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton7.perform(scrollTo(), click());
    }
    */

    @Test
    public void mainActivityRemoveGoalTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add_sport), withContentDescription("Add sport"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.sport_name),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Tennis"), closeSoftKeyboard());

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.distance_goal), withText("Distance Goal"),
                        childAtPosition(
                                allOf(withId(R.id.edit_name),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.add_sport), withText("Add sport"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_name),
                                        3),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction tabView = onView(
                allOf(withContentDescription("My Goals"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sliding_tabs),
                                        0),
                                2),
                        isDisplayed()));
        tabView.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.add_goal), withContentDescription("Add goal"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager)),
                                1),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.activity_description),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        appCompatEditText2.perform(scrollTo(), replaceText("Devenir champion"), closeSoftKeyboard());

        ViewInteraction materialCheckBox2 = onView(
                allOf(withId(R.id.time_goal), withText("Time goal"),
                        childAtPosition(
                                allOf(withId(R.id.time_container),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0)));
        materialCheckBox2.perform(scrollTo(), click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.numPad6), withText("6"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.numPad),
                                        1),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.add_goal), withText("Add goal"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1)));
        materialButton4.perform(scrollTo(), click());

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.goals_list),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)))
                .atPosition(0);
        linearLayout.perform(longClick());

        ViewInteraction materialButton5 = onView(
                allOf(withId(android.R.id.button1), withText("remove"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton5.perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }


}
