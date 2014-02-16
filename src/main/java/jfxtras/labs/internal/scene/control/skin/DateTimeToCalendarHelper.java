/**
 * DateTimeToCalendarHelper.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.internal.scene.control.skin;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.util.DateTimeUtil;

/**
 *
 * @author user
 */
public class DateTimeToCalendarHelper {

	/**
	 * 
	 * @param calendarProperty
	 * @param localDateProperty
	 * @param localeProperty 
	 */
	static public void syncLocalDate(ObjectProperty<Calendar> calendarProperty, ObjectProperty<LocalDate> localDateProperty, ObjectProperty<Locale> localeProperty)
	{
		// initial
		calendarProperty.set(localDateProperty.get() == null ? null : DateTimeUtil.createCalendarFromLocalDate(localDateProperty.get(), localeProperty.get()));
		
		// forward changes from calendar
		calendarProperty.addListener( (ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue) -> {
			localDateProperty.set(DateTimeUtil.createLocalDateFromCalendar(newValue)); 
		});
		
		// forward changes to calendar
		localDateProperty.addListener( (ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue) -> {
			calendarProperty.set(newValue == null ? null : DateTimeUtil.createCalendarFromLocalDate(newValue, localeProperty.get()));
		});
	}

	/**
	 * 
	 * @param calendarProperty
	 * @param localDateProperty
	 * @param localeProperty 
	 */
	static public void syncLocalDateTime(ObjectProperty<Calendar> calendarProperty, ObjectProperty<LocalDateTime> localDateTimeProperty, ObjectProperty<Locale> localeProperty)
	{
		// initial
		calendarProperty.set(localDateTimeProperty.get() == null ? null : DateTimeUtil.createCalendarFromLocalDateTime(localDateTimeProperty.get(), localeProperty.get()));
		
		// forward changes from calendar
		calendarProperty.addListener( (ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue) -> {
			localDateTimeProperty.set(DateTimeUtil.createLocalDateTimeFromCalendar(newValue)); 
		});
		
		// forward changes to calendar
		localDateTimeProperty.addListener( (ObservableValue<? extends LocalDateTime> observableValue, LocalDateTime oldValue, LocalDateTime newValue) -> {
			calendarProperty.set(newValue == null ? null : DateTimeUtil.createCalendarFromLocalDateTime(newValue, localeProperty.get()));
		});
	}

	/**
	 * 
	 * @param calendars
	 * @param localDates
	 * @param localeProperty 
	 */
	static public void syncLocalDates(ObservableList<Calendar> calendars, ObservableList<LocalDate> localDates, ObjectProperty<Locale> localeProperty)
	{
		// initial values
		for (LocalDate lLocalDate : localDates) {
			Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDate(lLocalDate, localeProperty.get());
			calendars.add(lCalendar);
		}
		
		// forward changes from calendar
		calendars.addListener( (ListChangeListener.Change<? extends Calendar> change) -> {
			while (change.next())
			{
				for (Calendar lCalendar : change.getRemoved())
				{
					LocalDate lLocalDate = DateTimeUtil.createLocalDateFromCalendar(lCalendar);
					if (localDates.contains(lLocalDate)) {
						localDates.remove(lLocalDate);
					}				
				}
				for (Calendar lCalendar : change.getAddedSubList()) 
				{
					LocalDate lLocalDate = DateTimeUtil.createLocalDateFromCalendar(lCalendar);
					if (localDates.contains(lLocalDate) == false) {
						localDates.add(lLocalDate);
					}
				}
			}
		});
		
		// forward changes to calendar
		localDates.addListener( (ListChangeListener.Change<? extends LocalDate> change) -> {
			while (change.next()) {
				for (LocalDate lLocalDate : change.getRemoved()) {
					Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDate(lLocalDate, localeProperty.get());
					if (calendars.contains(lCalendar)) {
						calendars.remove(lCalendar);
					}
				}
				for (LocalDate lLocalDate : change.getAddedSubList()) {
					Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDate(lLocalDate, localeProperty.get());
					if (calendars.contains(lCalendar) == false) {
						calendars.add(lCalendar);
					}
				}
			}
		});
	}
	
		/**
	 * 
	 * @param calendars
	 * @param localDateTimes
	 * @param localeProperty 
	 */
	static public void syncLocalDateTimes(ObservableList<Calendar> calendars, ObservableList<LocalDateTime> localDateTimes, ObjectProperty<Locale> localeProperty)
	{
		// initial values
		for (LocalDateTime lLocalDateTime : localDateTimes) {
			Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDateTime(lLocalDateTime, localeProperty.get());
			calendars.add(lCalendar);
		}
		
		// forward changes from calendar
		calendars.addListener( (ListChangeListener.Change<? extends Calendar> change) -> {
			while (change.next())
			{
				for (Calendar lCalendar : change.getRemoved())
				{
					LocalDateTime lLocalDateTime = DateTimeUtil.createLocalDateTimeFromCalendar(lCalendar);
					if (localDateTimes.contains(lLocalDateTime)) {
						localDateTimes.remove(lLocalDateTime);
					}				
				}
				for (Calendar lCalendar : change.getAddedSubList()) 
				{
					LocalDateTime lLocalDateTime = DateTimeUtil.createLocalDateTimeFromCalendar(lCalendar);
					if (localDateTimes.contains(lLocalDateTime) == false) {
						localDateTimes.add(lLocalDateTime);
					}
				}
			}
		});
		
		// forward changes to calendar
		localDateTimes.addListener( (ListChangeListener.Change<? extends LocalDateTime> change) -> {
			while (change.next()) {
				for (LocalDateTime lLocalDateTime : change.getRemoved()) {
					Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDateTime(lLocalDateTime, localeProperty.get());
					if (calendars.contains(lCalendar)) {
						calendars.remove(lCalendar);
					}
				}
				for (LocalDateTime lLocalDateTime : change.getAddedSubList()) {
					Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDateTime(lLocalDateTime, localeProperty.get());
					if (calendars.contains(lCalendar) == false) {
						calendars.add(lCalendar);
					}
				}
			}
		});
	}

	static public void syncDateTimeFormatter(ObjectProperty<DateFormat> dateFormatProperty, ObjectProperty<DateTimeFormatter> dateTimeFormatterProperty) {
		dateFormatProperty.set( dateTimeFormatterProperty.get() == null ? null : new DateTimeFormatterToDateFormatWrapper( dateTimeFormatterProperty.get() ));
		dateTimeFormatterProperty.addListener( (observable) -> {
			dateFormatProperty.set( new DateTimeFormatterToDateFormatWrapper( dateTimeFormatterProperty.get() ));
		});
	}
	
	static public void syncDateTimeFormatters(ListProperty<DateFormat> dateFormatsProperty, ListProperty<DateTimeFormatter> dateTimeFormattersProperty) {
		final Map<DateTimeFormatter, DateTimeFormatterToDateFormatWrapper> map = new WeakHashMap<>();
		
		// initial values
		for (DateTimeFormatter lDateTimeFormatter : dateTimeFormattersProperty) {
			DateTimeFormatterToDateFormatWrapper lDateTimeFormatterToDateFormatWrapper = new DateTimeFormatterToDateFormatWrapper( lDateTimeFormatter );
			map.put(lDateTimeFormatter, lDateTimeFormatterToDateFormatWrapper);
			dateFormatsProperty.add( lDateTimeFormatterToDateFormatWrapper );
		}
		
		// forward changes from localDate
		dateTimeFormattersProperty.addListener( (ListChangeListener.Change<? extends DateTimeFormatter> change) -> {
			while (change.next())
			{
				for (DateTimeFormatter lDateTimeFormatter : change.getRemoved())
				{
					DateTimeFormatterToDateFormatWrapper lDateTimeFormatterToDateFormatWrapper = map.remove(lDateTimeFormatter);
					dateFormatsProperty.remove(lDateTimeFormatterToDateFormatWrapper);
				}
				for (DateTimeFormatter lDateTimeFormatter : change.getAddedSubList()) 
				{
					DateTimeFormatterToDateFormatWrapper lDateTimeFormatterToDateFormatWrapper = new DateTimeFormatterToDateFormatWrapper( lDateTimeFormatter );
					map.put(lDateTimeFormatter, lDateTimeFormatterToDateFormatWrapper);
					dateFormatsProperty.add(lDateTimeFormatterToDateFormatWrapper);
				}
			}
		});
	}
	final Map<DateTimeFormatter, DateTimeFormatterToDateFormatWrapper> map = new WeakHashMap<>();

	/**
	 * 
	 */
	static class DateTimeFormatterToDateFormatWrapper extends DateFormat {

		public DateTimeFormatterToDateFormatWrapper(DateTimeFormatter dateTimeFormatter) {
			this.dateTimeFormatter = dateTimeFormatter;
		}
		final private DateTimeFormatter dateTimeFormatter;
		
		@Override
		public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
			LocalDate lLocalDate = DateTimeUtil.createLocaleDateFromDate(date);
			String s = this.dateTimeFormatter.format( lLocalDate );
			toAppendTo.append(s);
			return toAppendTo;
		}

		@Override
		public Date parse(String source, ParsePosition pos) {
			LocalDate lLocalDate = LocalDate.parse(source, this.dateTimeFormatter);
			Date lDate = DateTimeUtil.createDateFromLocalDate(lLocalDate);
			return lDate;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			return (this == obj);
		}
	}
	

}