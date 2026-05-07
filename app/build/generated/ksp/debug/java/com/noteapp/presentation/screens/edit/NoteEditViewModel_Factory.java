package com.noteapp.presentation.screens.edit;

import com.noteapp.domain.usecase.GetNoteByIdUseCase;
import com.noteapp.domain.usecase.InsertNoteUseCase;
import com.noteapp.domain.usecase.UpdateNoteUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class NoteEditViewModel_Factory implements Factory<NoteEditViewModel> {
  private final Provider<GetNoteByIdUseCase> getNoteByIdUseCaseProvider;

  private final Provider<InsertNoteUseCase> insertNoteUseCaseProvider;

  private final Provider<UpdateNoteUseCase> updateNoteUseCaseProvider;

  public NoteEditViewModel_Factory(Provider<GetNoteByIdUseCase> getNoteByIdUseCaseProvider,
      Provider<InsertNoteUseCase> insertNoteUseCaseProvider,
      Provider<UpdateNoteUseCase> updateNoteUseCaseProvider) {
    this.getNoteByIdUseCaseProvider = getNoteByIdUseCaseProvider;
    this.insertNoteUseCaseProvider = insertNoteUseCaseProvider;
    this.updateNoteUseCaseProvider = updateNoteUseCaseProvider;
  }

  @Override
  public NoteEditViewModel get() {
    return newInstance(getNoteByIdUseCaseProvider.get(), insertNoteUseCaseProvider.get(), updateNoteUseCaseProvider.get());
  }

  public static NoteEditViewModel_Factory create(
      Provider<GetNoteByIdUseCase> getNoteByIdUseCaseProvider,
      Provider<InsertNoteUseCase> insertNoteUseCaseProvider,
      Provider<UpdateNoteUseCase> updateNoteUseCaseProvider) {
    return new NoteEditViewModel_Factory(getNoteByIdUseCaseProvider, insertNoteUseCaseProvider, updateNoteUseCaseProvider);
  }

  public static NoteEditViewModel newInstance(GetNoteByIdUseCase getNoteByIdUseCase,
      InsertNoteUseCase insertNoteUseCase, UpdateNoteUseCase updateNoteUseCase) {
    return new NoteEditViewModel(getNoteByIdUseCase, insertNoteUseCase, updateNoteUseCase);
  }
}
