package com.aroraaman.myshopify.viewmodel;

import android.arch.lifecycle.ViewModel;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewModelFactoryTest {
    private ChildViewModel mModel = new ChildViewModel();

    @Test
    public void create_providerAvailable_returnsInstance() throws Exception {
        // Arrange
        Map<Class<? extends ViewModel>, javax.inject.Provider<ViewModel>> providers = new HashMap<>();
        providers.put(ChildViewModel.class, new Provider(mModel));

        ViewModelFactory sut = new ViewModelFactory(providers);

        // Act
        ChildViewModel viewModel = sut.create(ChildViewModel.class);

        // Assert
        assertThat(viewModel).isEqualTo(mModel);
    }

    @Test
    public void create_assignableProviderAvailable_returnsInstance() throws Exception {
        // Arrange
        Map<Class<? extends ViewModel>, javax.inject.Provider<ViewModel>> providers = new HashMap<>();
        providers.put(ChildViewModel.class, new Provider(mModel));

        ViewModelFactory sut = new ViewModelFactory(providers);

        // Act
        ParentViewModel viewModel = sut.create(ParentViewModel.class);

        // Assert
        assertThat(viewModel).isEqualTo(mModel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_noProviderAvailable_throwsException() throws Exception {
        // Arrange
        Map<Class<? extends ViewModel>, javax.inject.Provider<ViewModel>> providers = new HashMap<>();

        ViewModelFactory sut = new ViewModelFactory(providers);

        // Act
        ChildViewModel viewModel = sut.create(ChildViewModel.class);

        // Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_noAssignableProviderAvailable_throwsException() throws Exception {
        // Arrange
        Map<Class<? extends ViewModel>, javax.inject.Provider<ViewModel>> providers = new HashMap<>();
        providers.put(ParentViewModel.class, new Provider(new ParentViewModel()));

        ViewModelFactory sut = new ViewModelFactory(providers);

        // Act
        ChildViewModel viewModel = sut.create(ChildViewModel.class);

        // Assert
    }

    @Test(expected = RuntimeException.class)
    public void create_providerCannotBeCasted_throwsException() throws Exception {
        // Arrange
        Map<Class<? extends ViewModel>, javax.inject.Provider<ViewModel>> providers = new HashMap<>();
        providers.put(ParentViewModel.class, new Provider(new OtherViewModel()));

        ViewModelFactory sut = new ViewModelFactory(providers);

        // Act
        ParentViewModel viewModel = sut.create(ParentViewModel.class);

        // Assert
    }

    private class ParentViewModel extends ViewModel {
    }

    private class ChildViewModel extends ParentViewModel {
    }

    private class OtherViewModel extends ViewModel {
    }

    private class Provider implements javax.inject.Provider<ViewModel> {
        private final Object returnValue;

        private Provider(Object returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public ViewModel get() {
            return (ViewModel) returnValue;
        }
    }
}